package at.srfg.iasset.connector.featureStore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.catalyst.InternalRow;
import org.apache.spark.sql.catalyst.expressions.GenericInternalRow;
import org.apache.spark.sql.execution.streaming.LongOffset;
import org.apache.spark.sql.execution.streaming.Offset;
import org.apache.spark.sql.execution.streaming.Source;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.unsafe.types.UTF8String;

import scala.Option;
import scala.Some;
import scala.collection.JavaConverters;
import scala.collection.immutable.Map;

public class ArrayStream implements Source{
	
	private static HashMap<String, ArrayStream> instances;
	private SQLContext sqlContext;
	private List<Object[]> buffer = new ArrayList<>();
	private ReentrantLock reentrantLock = new ReentrantLock(true);
	public StructType schema;
	private int lastCommitted;
	private JavaSparkContext jsc;
	public String streamName;
	private LongOffset offset = new LongOffset(-1);
	@SuppressWarnings("rawtypes")
	private List<Function> converters;
	
	static {
		instances = new HashMap<>();
	}
	
	// For testing purposes
	private Runnable testsig;
	private int srate = 50; // Data points per second
	Thread testthread;
	
	public ArrayStream(SQLContext sqlContext, Map<String, String> parameters, StructType schema) {
		this.sqlContext = sqlContext;
		this.jsc = new JavaSparkContext(sqlContext.sparkContext());
		this.schema = schema;
		this.streamName = JavaConverters.mapAsJavaMap(parameters).get("name");
		instances.put(this.streamName, this);
		System.out.println("AAdded steam with name " + this.streamName);
		
		populateConverters();
		
		// For testing purposes
		if(this.streamName.equals("DemoSig1")) {
			this.testsig = new Testsig(srate, this);
			this.testthread = new Thread(this.testsig);
			this.testthread.start();
		} else if (this.streamName.equals("DemoSig2")) {
			this.testsig = new Testsig2(srate, this);
			this.testthread = new Thread(this.testsig);
			this.testthread.start();
		}
}
	
	public static HashMap<String, ArrayStream> getInstances(){
		return instances;
	}
	
	public void resumeCheckpoint(Path offsetPath, File offsetDir) throws IOException {		
		
		List<File> flist = Arrays.asList(offsetDir.listFiles());
		
		int latestOffset = flist
				.stream()
				.mapToInt(v -> Integer.parseInt(v.toString()))
				.max()
				.getAsInt();
		File offsetFile = new File(Paths.get(offsetPath.toString(), String.valueOf(latestOffset)).toString());
		BufferedReader br = new BufferedReader(new FileReader(offsetFile));
		String lastOffset =  br 
				.lines()
				.reduce((first, second) -> second)
				.orElse(null);
		br.close();
		if (offset != null)
			lastCommitted = Integer.parseInt(lastOffset);	
 	}
	
	public void pushBuffer(Object[] item) {
		try {
			reentrantLock.lock();
			buffer.add(item);
			offset = offset.$plus(1);
			System.out.println("IIncreased offset to " + offset + " current buffer size = " + buffer.size());
		} 
		finally {
			reentrantLock.unlock();
		}
	}
		
	@Override
	public void commit(Offset end) {
		System.out.println("Comitting...");
		
		int offset_end = (int) Long.parseLong(end.json()) + 1;
        System.out.println("Resetting lastCommitted from " + lastCommitted + " to " + offset_end + "\n\n");
        lastCommitted = offset_end;
	}

	@Override
	public void stop() {
		instances.remove(this.streamName);
		
		if(this.streamName.equals("DemoSig1") || this.streamName.equals("DemoSig2")) { 
		// For testing purposes
		System.out.println("Shutting down Testsig");
		//testsig.stop();
		}
	}

	@Override
	public Dataset<Row> getBatch(Option<Offset> start, Offset end) {
		System.out.println("Getting Batch...");
				
		JavaRDD<InternalRow> rdd;
		long s = (start.isDefined()) ? Long.parseLong(start.get().json()) : -1;
		long e = Long.parseLong(end.json());
		
		int actualStart = (int) s - lastCommitted + 1;
		int actualEnd = (int) e - lastCommitted + 1;
	
		try {
			System.out.println("Trying to achieve Lock and extract " +  actualStart + " - "  + actualEnd + " and last commited = " + lastCommitted + " with indexes " + s + " and " + e + "\n\n");
			reentrantLock.lock();
			System.out.println("Acieved Lock... \n\n");
			System.out.println("Buffer size = " + buffer.size() + "\n\n");
			List<Object[]> rowChunk = buffer.subList(actualStart, actualEnd);
            System.out.println("Cutting Buffer from " + actualEnd + " to " + buffer.size());
            buffer = buffer.subList(actualEnd, buffer.size());
            List<InternalRow> intRowChunk =  rowChunk.stream()
            		.map(row -> new GenericInternalRow(convertRow(row)))
            		.collect(Collectors.toList());
			rdd = jsc.parallelize(intRowChunk);
		} finally {
			reentrantLock.unlock();
		}
		
		System.out.println("Returning batch " + rdd + "\n\n");
		return sqlContext.sparkSession().internalCreateDataFrame(rdd.rdd(), schema, true);	
		
	}
	
	@SuppressWarnings("unchecked")
	public Object[] convertRow(Object[] rowval) {
		for(int i=0; i  < rowval.length; i++) {
			rowval[i] = converters.get(i).apply(rowval[i]);
		}
		return rowval;
	}

	@Override
	public Option<Offset> getOffset() {
		System.out.println("\n GGetting offset.... " + offset.offset() + "\n\n");
		return (offset.offset() == -1) ? Option.apply(null) : new Some<Offset>(offset); 
	}

	@Override
	public StructType schema() {
		return this.schema;

	}
	
	// Not necessary with RowFactory
	private void populateConverters() {
        StructField[] fields = this.schema.fields();
        converters = new ArrayList<>(fields.length);
        for (StructField structField : fields) {

            if (structField.dataType() == DataTypes.StringType)
                converters.add(stringConverter);
            else if (structField.dataType() == DataTypes.IntegerType)
                converters.add(intConverter);
            else if (structField.dataType() == DataTypes.DoubleType)
                converters.add(doubleConverter);
            else if (structField.dataType() == DataTypes.TimestampType)
            	converters.add(stringConverter);
            else if (structField.dataType() == DataTypes.LongType)
            	converters.add(longConverter);
            
        }
    }
	
	private Function<String, UTF8String> stringConverter = (val) -> UTF8String.fromString(val);
    private Function<String, Integer> intConverter = (val) -> Integer.parseInt(val);
    private Function<String, Double> doubleConverter = (val) -> Double.parseDouble(val);
    private Function<String, Long> longConverter = (val) -> Timestamp.valueOf(val).getTime();
        
  

}