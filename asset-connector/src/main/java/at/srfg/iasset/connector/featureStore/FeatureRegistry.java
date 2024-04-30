package at.srfg.iasset.connector.featureStore;

import static org.apache.spark.sql.functions.col;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang.ArrayUtils;
import org.apache.hadoop.shaded.org.apache.commons.io.FileUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.ForeachWriter;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.streaming.DataStreamReader;
import org.apache.spark.sql.streaming.DataStreamWriter;
import org.apache.spark.sql.streaming.StreamingQueryException;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.eclipse.digitaltwin.aas4j.v3.model.EventPayload;

import at.srfg.iasset.connector.component.AASComponent;
import at.srfg.iasset.messaging.EventHandler;
import at.srfg.iasset.messaging.exception.MessagingException;
import at.srfg.iasset.repository.model.Sensor;

public class FeatureRegistry{
	
	private SparkSession spark;
	
	public FeatureRegistry(String appName, String hadoopHomeDir, String checkpointDir) throws IOException {
		FileUtils.deleteDirectory(new File(checkpointDir));
		System.setProperty("hadoop.home.dir", hadoopHomeDir);
//		Logger.getLogger("org.apache").setLevel(Level.WARN);
				
		this.spark = SparkSession.builder()
				.appName(appName)
				.master("local[*]")
				.config("spark.scheduler.mode", "FAIR")
				.getOrCreate();
	}
	
	public void closeRegistry() {
		spark.close();
	}
	
	/*
	 * Inputs
	 */
	
	public Dataset<Row> registerEventInput(AASComponent i40Component, String semanticId, List<String> variableNames, List<DataType> dataTypes) throws MessagingException{
		int nVariables = variableNames.size(); 
		StructField[] structFields = new StructField[nVariables];
		for(int i =0; i < nVariables; i++)
			structFields[i] = new StructField(variableNames.get(i), dataTypes.get(i), true, Metadata.empty());
		StructType schema = new StructType(structFields);
		
		DataStreamReader dr = spark
				.readStream()
				.format("at.srfg.iasset.connector.featureStore.ArraySource")
				.option("name", semanticId)
				.schema(schema);
		
		Dataset<Row> dt = dr
				 .load()
				 .withColumn("timestamp", functions.to_timestamp(col("Timestamp_str")))
				 .drop(col("Timestamp_str"));
			
		i40Component.registerCallback(semanticId, new EventHandler<Sensor>() {

			@Override
			public void onEventMessage(EventPayload eventPayload, Sensor payload) {
				System.out.println("MMeasured the following value: " + payload.getFeatureableValue());
				ArrayStream stream = ArrayStream.getInstances().get(semanticId);
				if(stream != null) {
					stream.pushBuffer(ArrayUtils.addAll(new String[] {payload.getTimestampCreated()}, payload.getFeatureableValue()));
					System.out.println("SSuccessfully pushed value");
				}
			}
		});
		return dt;
	}
	
	
	public Dataset<Row> registerDemoInput(String demoSignalName, List<String> variableNames, List<DataType> dataTypes){
		int nVariables = variableNames.size(); 
		StructField[] structFields = new StructField[nVariables];
		for(int i =0; i < nVariables; i++)
			structFields[i] = new StructField(variableNames.get(i), dataTypes.get(i), true, Metadata.empty());
		StructType schema = new StructType(structFields);
		
		DataStreamReader dr = spark
				.readStream()
				.format("at.srfg.iasset.connector.featureStore.ArraySource")
				.option("name", demoSignalName)
				.schema(schema);
		
		Dataset<Row> dt = dr
				 .load()
				 .withColumn("timestamp", functions.to_timestamp(col("Timestamp_str")))
				 .drop(col("Timestamp_str"));
		
		return dt;
	}
	

	
	/**
	 * Pipeline
	 */
	
	public FeaturePipeline registerPipeline(Dataset<Row> input, String name) {
		return new FeaturePipeline(input, name);
	}
	
	/**
	 * Monitoring
	 */
	
	public FeaturePipeline registerMonitoring(Dataset<Row> dataset, String featureName, Double eta, Double lambda) {
		FeatureMonitoring monitoring = new FeatureMonitoring(dataset, lambda);
		Dataset<Row> monitoringOutput = monitoring.martingaleTest(featureName, eta);
		return new FeaturePipeline(monitoringOutput, featureName + "_monitoring");
	}
	
	
	/**
	 *  Sinks
	 */
	
	public DataStreamWriter<Row> registerConsoleSink(FeaturePipeline pipeline, String checkpointlocation){
		return pipeline
				.getDataset()
				.writeStream()
				.outputMode("append")
				.format("Console")
				.option("checkpointLocation", checkpointlocation);
	}
	
	public DataStreamWriter<Row> registerRedisSink(FeaturePipeline pipeline, ForeachWriter<Row> redisWriter){
		return pipeline
				.getDataset()
				.writeStream()
				.outputMode("append")
				.foreach(redisWriter);
	}
	
	public DataStreamWriter<Row> registerDataSink(FeaturePipeline pipeline, String filetype, String path, String checkpointLocation ){
		String[] possibleFileSinks = {"csv", "json", "orc", "parquet"};
		assert Arrays.stream(possibleFileSinks).anyMatch(filetype::equals): "filetype must be one of 'csv', 'json', 'orc', 'parquet'";
		
		return pipeline
				.getDataset()
				.writeStream()
				.outputMode("append")
				.format(filetype)
				.option("path", path)
				.option("checkpointLocation", checkpointLocation);
	}
	
	
	/**
	 * Run
	 * 
	 * @throws TimeoutException
	 * @throws StreamingQueryException
	 * @throws IOException
	 */
	
	
	public void runQuery(DataStreamWriter<Row> writer) throws TimeoutException, StreamingQueryException {
		writer.start();
	}

	public void awaitTermination() throws StreamingQueryException {
		spark.streams().awaitAnyTermination();
	}
	
	
	
}
