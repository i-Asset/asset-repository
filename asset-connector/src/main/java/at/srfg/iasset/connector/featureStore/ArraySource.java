package at.srfg.iasset.connector.featureStore;

import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.execution.streaming.Source;
import org.apache.spark.sql.sources.DataSourceRegister;
import org.apache.spark.sql.sources.StreamSourceProvider;
import org.apache.spark.sql.types.StructType;

import scala.Option;
import scala.Tuple2;
import scala.collection.immutable.Map;

public class ArraySource implements StreamSourceProvider, DataSourceRegister {

	String shortname = "ArraySource";
	
	public ArraySource() {
	
	}
	
	@Override
	public String shortName() {
		return this.shortname;
	}

	@Override
	public Source createSource(SQLContext sqlContext, String metadataPath, Option<StructType> schema,
			String providerName, Map<String, String> parameters) {
		System.out.println("CCreating Source and schema is: " + schema.get());
		
		return new ArrayStream(sqlContext, parameters, schema.get());
	}

	@Override
	public Tuple2<String, StructType> sourceSchema(SQLContext sqlContext, Option<StructType> schema,
			String providerName, Map<String, String> parameters) {
		
		return new Tuple2<String, StructType>(this.shortname, schema.get() );
	}
}
