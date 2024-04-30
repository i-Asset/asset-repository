package at.srfg.iasset.connector.featureStore;

import java.util.HashMap;
import java.util.Map;

import org.apache.spark.sql.ForeachWriter;
import org.apache.spark.sql.Row;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

public class RedisSink {
	
	private RedisClient redisClient;
	private StatefulRedisConnection<String, String> connection;
	public RedisCommands<String, String> syncCommands;
	
	
	public RedisSink(String conn) {
		this.redisClient = RedisClient.create(conn);
		this.connection = redisClient.connect();
		this.syncCommands = connection.sync();
	}
	
	public void close() {
		connection.close();
		redisClient.shutdown();
	}
	
	public ForeachWriter<Row> getWriter(String featureName, String variableName ) {
		
		return new ForeachWriter<Row>() {

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
				messageBody.put("timestamp", value.getStruct(0).get(0).toString());
				messageBody.put(variableName, String.valueOf(value.getDouble(1)));
				syncCommands.xadd(featureName, messageBody);			
			}
			
		};
		
		
	}
	
}
