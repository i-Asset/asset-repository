package at.srfg.iasset.connector.featureStore;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.ForeachWriter;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.streaming.DataStreamWriter;
import org.apache.spark.sql.streaming.StreamingQueryException;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;

public class Receivertest {
	
	public static final String featureName = "RPeaks";
	public static final String featureName2 = "Resp";
	public static final String CHECKPOINT_LOCATION = "/home/sebastian/Documents/checkpoint";
	public static final String CHECKPOINT_LOCATION2 = "/home/sebastian/Documents/checkpoint";
	private static final double SRATE = 256; 
	private static final double SRATE_NEW = 120;
	private static final List<String> variableNames = new ArrayList<>(
			List.of("Timestamp_str", "field1", "field2", "group"));
	private static final List<String> variableNames2 = new ArrayList<>(
			List.of("Timestamp_str", "field1", "strangeness", "stage", "apnea"));
	public static final List<DataType> dataTypes = new ArrayList<>(
			List.of(DataTypes.StringType, DataTypes.DoubleType, DataTypes.DoubleType, DataTypes.DoubleType));
	public static final List<DataType> dataTypes2 = new ArrayList<>(
			List.of(DataTypes.StringType, DataTypes.DoubleType, DataTypes.DoubleType, DataTypes.StringType, DataTypes.StringType));
	
	// Redis Sink
	static RedisSink redisSink = new RedisSink("redis://localhost:7777");
	
	
	// Redis sink
	static ForeachWriter<Row> redisWriter = new ForeachWriter<Row>() {

		private static final long serialVersionUID = 1L;

		@Override
		public void close(Throwable errorOrNull) {
		}

		@Override
		public boolean open(long partitionId, long epochId) {
			return true;
		}

		@Override
		public void process(Row value) {
			Map<String, String> messageBody = new HashMap<>();
			messageBody.put("timestamp", value.get(0).toString());
			messageBody.put("ecg", String.valueOf(value.get(1)));
			redisSink.syncCommands.xadd(featureName, messageBody);			
		}
		
	};
	
	
	public static void main(String[] args) throws TimeoutException, StreamingQueryException, IOException {
			
		FeatureRegistry fregist = new FeatureRegistry("testapp", "/home/sebastian/hadoop", CHECKPOINT_LOCATION);
		Dataset<Row> df = fregist.registerDemoInput("DemoSig1", variableNames, dataTypes);
		FeaturePipeline pipeline = fregist.registerPipeline(df, featureName);
		Dataset<Row> df2 = fregist.registerDemoInput("DemoSig2", variableNames2, dataTypes2);
		FeaturePipeline pipeline2 = fregist.registerPipeline(df2, featureName2);
		
		
		FeaturePipeline  results = pipeline.joinPipelines(pipeline2, "timestamp", "timestamp", "30 seconds", "30 seconds" , "1 second", "inner");
//				.lag("field1", 0.);
//				.resample(SRATE_NEW)
//				.bandpassFilter(SRATE_NEW, 5D, 15D, 4)
//				.lag()
//				.group_time(150, 1)
//				.group_by("group")
//				.median("field2");
//				.pow(2);
//				.groupMean(150, 1);
		
		DataStreamWriter<Row> writer = fregist.registerConsoleSink(results, CHECKPOINT_LOCATION);
//		DataStreamWriter<LineWithTimestamp> writer = fregist.registerRedisSink(results, redisWriter);
//		DataStreamWriter<Row> writer = fregist.registerDataSink(pipeline, "csv", "/home/sebastian/Documents/testStream", CHECKPOINT_LOCATION);
		
		//DataStreamWriter<Row> writer2 = fregist.registerRedisSink(pipeline2, redisWriter);
		fregist.runQuery(writer);
		
		fregist.awaitTermination();
	}
	
}
