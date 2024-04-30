package at.srfg.iasset.connector.featureStore;

import static org.apache.spark.sql.functions.col;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.spark.api.java.function.FlatMapGroupsWithStateFunction;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.RelationalGroupedDataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.streaming.GroupState;
import org.apache.spark.sql.streaming.GroupStateTimeout;
import org.apache.spark.sql.streaming.OutputMode;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import at.srfg.iasset.connector.featureStore.JavaStructuredSessionization.FeatureStatus;
import at.srfg.iasset.connector.featureStore.JavaStructuredSessionization.SamplingStatus;
import uk.me.berndporr.iirj.Butterworth;

public class FeaturePipeline implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	private Dataset<Row> dataset;
	private RelationalGroupedDataset relationalDataset;
	private String piplineName;
	private static Butterworth filter;
	private DatasetGrouped datasetGrouped;
	
	public FeaturePipeline(Dataset<Row> input, String name) {
		this.dataset = input;
		this.piplineName = name;
		this.datasetGrouped = DatasetGrouped.UNGROUPED;
	}
	
	private enum DatasetGrouped {
		UNGROUPED,
		KEYVAL,
		TIME
	}
			
	public Dataset<Row> getDataset(){
		return this.dataset;
	}
	
	public String getPiplineName() {
		return this.piplineName;
	}
	
		
	/*
	 * Grouping
	 */
	private MapFunction<Row, Timestamp> groupTime(){
		
		return new MapFunction<Row, Timestamp>(){
	
			private static final long serialVersionUID = 1L;
	
			@Override
			public Timestamp call(Row arg0) throws Exception {
				int ix = arg0.fieldIndex("timestamp");
				return arg0.getTimestamp(ix);
			}
		};
	}

	public FeaturePipeline group_time(Integer winlen, Integer olap) {
		datasetGrouped = DatasetGrouped.TIME;
		relationalDataset= dataset
				.withWatermark("timestamp", "500000 microseconds")
				.groupBy(
						functions.window(col("timestamp"), "500000 microseconds"), col("field2"));
//						functions.window(col("timestamp"), String.format("%d milliseconds", winlen), String.format("%d milliseconds", olap)), col("line"));
		
		
		return this;
	}
	
	public FeaturePipeline group_by(String key) {
		datasetGrouped = DatasetGrouped.KEYVAL;
		relationalDataset = dataset
				.withWatermark("timestamp", "500000 microseconds")
				.groupBy(key, "timestamp");
		return this;
	}
		
	/*
	 * Simple Functions
	 */
		
	
	public FeaturePipeline abs(String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn(newColName, functions.abs(col(ColName)));		
		if(!keep) {
			dataset = dataset.drop(col(ColName));
		} else {
			assert !ColName.equals(newColName): "Column names can't be equal if the old column is kept"; 
		}
		return this;
	}
	
	public FeaturePipeline acos(String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn(newColName, functions.acos(col(ColName)));		
		if(!keep) {
			dataset = dataset.drop(col(ColName));
		} else {
			assert !ColName.equals(newColName): "Column names can't be equal if the old column is kept"; 
		}
		return this;
	}
	
	public FeaturePipeline acosh(String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn(newColName, functions.acosh(col(ColName)));		
		if(!keep) {
			dataset = dataset.drop(col(ColName));
		} else {
			assert !ColName.equals(newColName): "Column names can't be equal if the old column is kept"; 
		}
		return this;
	}
	
	public FeaturePipeline asin(String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn(newColName, functions.asin(col(ColName)));		
		if(!keep) {
			dataset = dataset.drop(col(ColName));
		} else {
			assert !ColName.equals(newColName): "Column names can't be equal if the old column is kept"; 
		}
		return this;
	}
	
	public FeaturePipeline asinh (String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn(newColName, functions.asinh(col(ColName)));		
		if(!keep) {
			dataset = dataset.drop(col(ColName));
		} else {
			assert !ColName.equals(newColName): "Column names can't be equal if the old column is kept"; 
		}
		return this;
	}
	
	public FeaturePipeline atan(String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn(newColName, functions.atan(col(ColName)));		
		if(!keep) {
			dataset = dataset.drop(col(ColName));
		} else {
			assert !ColName.equals(newColName): "Column names can't be equal if the old column is kept"; 
		}
		return this;
	}
	
	public FeaturePipeline atanh(String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn(newColName, functions.atanh(col(ColName)));		
		if(!keep) {
			dataset = dataset.drop(col(ColName));
		} else {
			assert !ColName.equals(newColName): "Column names can't be equal if the old column is kept"; 
		}
		return this;
	}
	
	public FeaturePipeline ceil(Integer decimals, String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn("decimals", functions.lit(decimals))
				.withColumn(newColName, functions.ceil(col(ColName)));	
		if(!keep) {
			dataset = dataset
					.drop(col(ColName))
					.drop(col("decimals"));
		} else {
			dataset = dataset
					.drop(col("decimals"));
			assert !ColName.equals(newColName): "Columns names can't be equal if the old column is kept"; 
		}
		
		return this;
	}
	
	public FeaturePipeline cos(String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn(newColName, functions.cos(col(ColName)));		
		if(!keep) {
			dataset = dataset.drop(col(ColName));
		} else {
			assert !ColName.equals(newColName): "Columns names can't be equal if the old column is kept"; 
		}
		return this;
	}
	
	public FeaturePipeline cot(String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn(newColName, functions.cot(col(ColName)));		
		if(!keep) {
			dataset = dataset.drop(col(ColName));
		} else {
			assert !ColName.equals(newColName): "Columns names can't be equal if the old column is kept"; 
		}
		return this;
	}
	
	public FeaturePipeline cosh(String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn(newColName, functions.cot(col(ColName)));		
		if(!keep) {
			dataset = dataset.drop(col(ColName));
		} else {
			assert !ColName.equals(newColName): "Columns names can't be equal if the old column is kept"; 
		}
		return this;
	}
	
	public FeaturePipeline date_add(Integer days, String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn(newColName, functions.date_add(col(ColName), days));		
		if(!keep) {
			dataset = dataset.drop(col(ColName));
		} else {
			assert !ColName.equals(newColName): "Columns names can't be equal if the old column is kept"; 
		}
		return this;
	}
	
	public FeaturePipeline date_format(String format, String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn(newColName, functions.date_format(col(ColName), format));
		if(!keep) {
			dataset = dataset.drop(col(ColName));
		} else {
			assert !ColName.equals(newColName): "Columns names can't be equal if the old column is kept"; 
		}		
		return this;
	}
	
	public FeaturePipeline date_sub(Integer days, String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn(newColName, functions.date_sub(col(ColName), days));
		if(!keep) {
			dataset = dataset.drop(col(ColName));
		} else {
			assert !ColName.equals(newColName): "Columns names can't be equal if the old column is kept"; 
		}
		return this;
	}
	
	public FeaturePipeline day(String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn(newColName, functions.day(col(ColName)));
		if(!keep) {
			dataset = dataset.drop(col(ColName));
		} else {
			assert !ColName.equals(newColName): "Columns names can't be equal if the old column is kept"; 
		}		
		return this;
	}
	
	public FeaturePipeline degrees(String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn(newColName, functions.degrees(col(ColName)));
		if(!keep) {
			dataset = dataset.drop(col(ColName));
		} else {
			assert !ColName.equals(newColName): "Columns names can't be equal if the old column is kept"; 
		}
		return this;
	}
	
	public FeaturePipeline exp(String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn(newColName, functions.exp(col(ColName)));
		if(!keep) {
			dataset = dataset.drop(col(ColName));
		} else {
			assert !ColName.equals(newColName): "Columns names can't be equal if the old column is kept"; 
		}
		return this;
	}
	
	public FeaturePipeline factorial(String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn(newColName, functions.factorial(col(ColName)));
		if(!keep) {
			dataset = dataset.drop(col(ColName));
		} else {
			assert !ColName.equals(newColName): "Columns names can't be equal if the old column is kept"; 
		}
		return this;
	}
	
	public FeaturePipeline floor(Integer decimals, String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn("decimals", functions.lit(decimals))
				.withColumn(newColName, functions.factorial(col(ColName)));
		if(!keep) {
			dataset = dataset
					.drop(col(ColName))
					.drop(col("decimals"));
		} else {
			dataset = dataset.drop(col("decimals"));
			assert !ColName.equals(newColName): "Columns names can't be equal if the old column is kept"; 
		}
		return this;
	}
	
	public FeaturePipeline hours(String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn(newColName, functions.hours(col(ColName)));
		if(!keep) {
			dataset = dataset.drop(col(ColName));
		} else {
			assert !ColName.equals(newColName): "Columns names can't be equal if the old column is kept"; 
		}
		return this;
	}
	
	public FeaturePipeline instr(String substring, String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn(newColName, functions.instr(col(ColName), substring));
		if(!keep) {
			dataset = dataset.drop(col(ColName));
		} else {
			assert !ColName.equals(newColName): "Columns names can't be equal if the old column is kept"; 
		}
		return this;
	}
	
	public FeaturePipeline isnan(String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn(newColName, functions.isnan(col(ColName)));
		if(!keep) {
			dataset = dataset.drop(col(ColName));
		} else {
			assert !ColName.equals(newColName): "Columns names can't be equal if the old column is kept"; 
		}
		return this;
	}
	
	public FeaturePipeline isnull(String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn(newColName, functions.isnull(col(ColName)));
		if(!keep) {
			dataset = dataset.drop(col(ColName));
		} else {
			assert !ColName.equals(newColName): "Columns names can't be equal if the old column is kept"; 
		}
		return this;
	}
	
	public FeaturePipeline lcase(String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn(newColName, functions.lcase(col(ColName)));
		if(!keep) {
			dataset = dataset.drop(col(ColName));
		} else {
			assert !ColName.equals(newColName): "Columns names can't be equal if the old column is kept"; 
		}
		return this;
	}
	
	public FeaturePipeline len(String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn(newColName, functions.len(col(ColName)));
		if(!keep) {
			dataset = dataset.drop(col(ColName));
		} else {
			assert !ColName.equals(newColName): "Columns names can't be equal if the old column is kept"; 
		}
		return this;
	}
	
	public FeaturePipeline ln(String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn(newColName, functions.ln(col(ColName)));
		if(!keep) {
			dataset = dataset.drop(col(ColName));
		} else {
			assert !ColName.equals(newColName): "Columns names can't be equal if the old column is kept"; 
		}
		return this;
	}
	
	public FeaturePipeline log(Integer base, String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn(newColName, functions.log(Double.valueOf(base), col(ColName)));
		if(!keep) {
			dataset = dataset.drop(col(ColName));
		} else {
			assert !ColName.equals(newColName): "Columns names can't be equal if the old column is kept"; 
		}
		return this;
	}
	
	public FeaturePipeline ltrim(String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn(newColName, functions.ltrim(col(ColName)));
		if(!keep) {
			dataset = dataset.drop(col(ColName));
		} else {
			assert !ColName.equals(newColName): "Columns names can't be equal if the old column is kept"; 
		}
		return this;
	}
	
	public FeaturePipeline minute(String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn(newColName, functions.minute(col(ColName)));
		if(!keep) {
			dataset = dataset.drop(col(ColName));
		} else {
			assert !ColName.equals(newColName): "Columns names can't be equal if the old column is kept"; 
		}
		return this;
	}
	
	public FeaturePipeline month(String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn(newColName, functions.month(col(ColName)));
		if(!keep) {
			dataset = dataset.drop(col(ColName));
		} else {
			assert !ColName.equals(newColName): "Columns names can't be equal if the old column is kept"; 
		}
		return this;
	}
	
	public FeaturePipeline pow(Integer exponent, String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn(newColName, functions.pow(col(ColName), exponent));
		if(!keep) {
			dataset = dataset.drop(col(ColName));
		} else {
			assert !ColName.equals(newColName): "Columns names can't be equal if the old column is kept"; 
		}
		return this;
	}
	
	public FeaturePipeline radians(String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn(newColName, functions.radians(col(ColName)));
		if(!keep) {
			dataset = dataset.drop(col(ColName));
		} else {
			assert !ColName.equals(newColName): "Columns names can't be equal if the old column is kept"; 
		}
		return this;
	}
	
	public FeaturePipeline regexp_extract(String exp, String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn("pattern", functions.lit(exp))
				.withColumn(newColName, functions.regexp_extract_all(col(ColName), col("pattern")));
		if(!keep) {
			dataset = dataset
					.drop(col(ColName))
					.drop(col("pattern"));
		} else {
			dataset = dataset.drop(col("pattern"));
			assert !ColName.equals(newColName): "Columns names can't be equal if the old column is kept"; 
		}	
		return this;
	}
	
	public FeaturePipeline regexp_like(String exp, String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn("pattern", functions.lit(exp))
				.withColumn(newColName, functions.regexp_like(col(ColName), col("pattern")));
		if(!keep) {
			dataset = dataset
					.drop(col(ColName))
					.drop(col("pattern"));
		} else {
			dataset = dataset.drop(col("pattern"));
			assert !ColName.equals(newColName): "Columns names can't be equal if the old column is kept"; 
		}
		return this;
	}
	
	public FeaturePipeline regexp_replace(String exp, String replacement, String ColName, String newColName, Boolean keep ) {
		dataset = dataset
				.withColumn(newColName, functions.regexp_replace(col(ColName), exp, replacement ));
		if(!keep) {
			dataset = dataset.drop(col(ColName));
		} else {
			assert !ColName.equals(newColName): "Columns names can't be equal if the old column is kept"; 
		}
		return this;
	}
	
	public FeaturePipeline reverse(String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn(newColName, functions.reverse(col(ColName)));
		if(!keep) {
			dataset = dataset.drop(col(ColName));
		} else {
			assert !ColName.equals(newColName): "Columns names can't be equal if the old column is kept"; 
		}
		return this;
	}
	
	public FeaturePipeline round(Integer decimals, String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn(newColName, functions.round(col(ColName), decimals));
		if(!keep) {
			dataset = dataset.drop(col(ColName));
		} else {
			assert !ColName.equals(newColName): "Columns names can't be equal if the old column is kept"; 
		}
		return this;
	}
	
	public FeaturePipeline sec(String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn(newColName, functions.sec(col(ColName)));
		if(!keep) {
			dataset = dataset.drop(col(ColName));
		} else {
			assert !ColName.equals(newColName): "Columns names can't be equal if the old column is kept"; 
		}
		return this;
	}
	
	public FeaturePipeline shiftleft(Integer numBits, String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn(newColName, functions.shiftleft(col(ColName), numBits));
		if(!keep) {
			dataset = dataset.drop(col(ColName));
		} else {
			assert !ColName.equals(newColName): "Columns names can't be equal if the old column is kept"; 
		}
		return this;
	}
	
	public FeaturePipeline shiftright(Integer numBits, String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn(newColName, functions.shiftright(col(ColName), numBits));
		if(!keep) {
			dataset = dataset.drop(col(ColName));
		} else {
			assert !ColName.equals(newColName): "Columns names can't be equal if the old column is kept"; 
		}
		return this;
	}
	
	public FeaturePipeline sign(String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn(newColName, functions.sign(col(ColName)));
		if(!keep) {
			dataset = dataset.drop(col(ColName));
		} else {
			assert !ColName.equals(newColName): "Columns names can't be equal if the old column is kept"; 
		}
		return this;
	}
	
	public FeaturePipeline sin(String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn(newColName, functions.sin(col(ColName)));
		if(!keep) {
			dataset = dataset.drop(col(ColName));
		} else {
			assert !ColName.equals(newColName): "Columns names can't be equal if the old column is kept"; 
		}
		return this;
	}
	
	
	public FeaturePipeline sinh(String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn(newColName, functions.sinh(col(ColName)));
		if(!keep) {
			dataset = dataset.drop(col(ColName));
		} else {
			assert !ColName.equals(newColName): "Columns names can't be equal if the old column is kept"; 
		}
		return this;
	}
	
	public FeaturePipeline sqrt(Integer n, String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn(newColName, functions.pow(1/n, col(ColName)));
		if(!keep) {
			dataset = dataset.drop(col(ColName));
		} else {
			assert !ColName.equals(newColName): "Columns names can't be equal if the old column is kept"; 
		}	
		return this;
	}
	
	public FeaturePipeline substr(Integer pos, Integer len, String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn("pos", functions.lit(pos))
				.withColumn("len", functions.lit(len))
				.withColumn(newColName, functions.substr(col(ColName), col("pos"), col("len")));
		if(!keep) {
			dataset = dataset
					.drop(col(ColName))
					.drop(col("pos"))
					.drop(col("line"));
		} else {
			dataset = dataset
					.drop(col("pos"))
					.drop(col("line"));
			assert !ColName.equals(newColName): "Columns names can't be equal if the old column is kept"; 
		}
		return this;
	}
	
	public FeaturePipeline tan(String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn(newColName, functions.tan(col(ColName)));
		if(!keep) {
			dataset = dataset.drop(col(ColName));
		} else {
			assert !ColName.equals(newColName): "Columns names can't be equal if the old column is kept"; 
		}
		return this;
	}
	
	public FeaturePipeline tanh(String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn(newColName, functions.tanh(col(ColName)));
		if(!keep) {
			dataset = dataset.drop(col(ColName));
		} else {
			assert !ColName.equals(newColName): "Columns names can't be equal if the old column is kept"; 
		}
		return this;
	}
	
	public FeaturePipeline ucase(String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn(newColName, functions.ucase(col(ColName)));
		if(!keep) {
			dataset = dataset.drop(col(ColName));
		} else {
			assert !ColName.equals(newColName): "Columns names can't be equal if the old column is kept"; 
		}
		return this;
	}
	
	public FeaturePipeline weekday(String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn(newColName, functions.weekday(col(ColName)));
		if(!keep) {
			dataset = dataset.drop(col(ColName));
		} else {
			assert !ColName.equals(newColName): "Columns names can't be equal if the old column is kept"; 
		}
		return this;
	}
	
	public FeaturePipeline year(String ColName, String newColName, Boolean keep) {
		dataset = dataset
				.withColumn(newColName, functions.year(col(ColName)));
		if(!keep) {
			dataset = dataset.drop(col(ColName));
		} else {
			assert !ColName.equals(newColName): "Columns names can't be equal if the old column is kept"; 
		}
		return this;
	}
	
	/*
	 * Aggregate functions
	 */
	
	public FeaturePipeline avg(String colName) {
		switch(datasetGrouped) {
		case TIME:
			dataset = relationalDataset
				.agg(functions.avg(colName))
				.select(col("window"), col("avg(" + colName + ")"))
				.withColumn("window_start", col("window").getField("start"))
				.withColumn("window_end", col("window").getField("end"))
				.drop(col("window"));
			break;
	
		case KEYVAL:
			dataset = relationalDataset
				.agg(functions.avg(colName))
				.select(col("group"), col("avg(" + colName + ")"));
			break;
			
		case UNGROUPED:
			throw new IllegalArgumentException("Watermark needs to be specified when dataset is not grouped");
		}
		
		datasetGrouped = DatasetGrouped.UNGROUPED;
		return this;
	}
		
	public FeaturePipeline avg(String colName, Integer watermarkSeconds, Integer watermarkMicroseconds){
		switch(datasetGrouped) {
		case TIME:
			throw new IllegalArgumentException("Watermark cant be respecified after grouping");
		case KEYVAL:
			throw new IllegalArgumentException("Watermark cant be respecified after grouping");
		case UNGROUPED:
			assert watermarkSeconds == null || watermarkMicroseconds == null: "Only Seconds OR Microseconds can be specified for Watermark";
			assert watermarkSeconds != null || watermarkMicroseconds != null: "Either Seconds OR Microseconds mmust be specified for Watermark";
			
			String watermarkString = (watermarkSeconds == null) ? String.format("%d microseconds", watermarkMicroseconds) : String.format("%d seconds", watermarkSeconds);
			
			dataset = dataset
				.withColumn("group_all", functions.lit(0))
				.withWatermark("timestamp", watermarkString)
				.groupBy("group_all", "timestamp")
				.agg(functions.avg(colName))
				.select(col("avg(" + colName + ")"));
			break;
		}
		
		datasetGrouped = DatasetGrouped.UNGROUPED;
		return this;
	}
	
	public FeaturePipeline count(String colName) {
		switch(datasetGrouped) {
		case TIME:
			dataset = relationalDataset
				.agg(functions.count(colName))
				.select(col("window"), col("count(" + colName + ")"))
				.withColumn("window_start", col("window").getField("start"))
				.withColumn("window_end", col("window").getField("end"))
				.drop(col("window"));
			break;
	
		case KEYVAL:
			dataset = relationalDataset
				.agg(functions.avg(colName))
				.select(col("group"), col("count(" + colName + ")"));
			break;
			
		case UNGROUPED:
			throw new IllegalArgumentException("Watermark needs to be specified when dataset is not grouped");
		}
		
		datasetGrouped = DatasetGrouped.UNGROUPED;
		return this;
	}
		
	public FeaturePipeline count(String colName, Integer watermarkSeconds, Integer watermarkMicroseconds){
		switch(datasetGrouped) {
		case TIME:
			throw new IllegalArgumentException("Watermark cant be respecified after grouping");
		case KEYVAL:
			throw new IllegalArgumentException("Watermark cant be respecified after grouping");
		case UNGROUPED:
			assert watermarkSeconds == null || watermarkMicroseconds == null: "Only Seconds OR Microseconds can be specified for Watermark";
			assert watermarkSeconds != null || watermarkMicroseconds != null: "Either Seconds OR Microseconds mmust be specified for Watermark";
			
			String watermarkString = (watermarkSeconds == null) ? String.format("%d microseconds", watermarkMicroseconds) : String.format("%d seconds", watermarkSeconds);
			
			dataset = dataset
				.withColumn("group_all", functions.lit(0))
				.withWatermark("timestamp", watermarkString)
				.groupBy("group_all", "timestamp")
				.agg(functions.count(colName))
				.select(col("count(" + colName + ")"));
			break;
		}
		
		datasetGrouped = DatasetGrouped.UNGROUPED;
		return this;
	}
	
	public FeaturePipeline count_distinct(String colName) {
		switch(datasetGrouped) {
		case TIME:
			dataset = relationalDataset
				.agg(functions.countDistinct(colName))
				.select(col("window"), col("countDistinct(" + colName + ")"))
				.withColumn("window_start", col("window").getField("start"))
				.withColumn("window_end", col("window").getField("end"))
				.drop(col("window"));
			break;
	
		case KEYVAL:
			dataset = relationalDataset
				.agg(functions.avg(colName))
				.select(col("group"), col("countDistinct(" + colName + ")"));
			break;
			
		case UNGROUPED:
			throw new IllegalArgumentException("Watermark needs to be specified when dataset is not grouped");
		}
		
		datasetGrouped = DatasetGrouped.UNGROUPED;
		return this;
	}
		
	public FeaturePipeline count_distinct(String colName, Integer watermarkSeconds, Integer watermarkMicroseconds){
		switch(datasetGrouped) {
		case TIME:
			throw new IllegalArgumentException("Watermark cant be respecified after grouping");
		case KEYVAL:
			throw new IllegalArgumentException("Watermark cant be respecified after grouping");
		case UNGROUPED:
			assert watermarkSeconds == null || watermarkMicroseconds == null: "Only Seconds OR Microseconds can be specified for Watermark";
			assert watermarkSeconds != null || watermarkMicroseconds != null: "Either Seconds OR Microseconds mmust be specified for Watermark";
			
			String watermarkString = (watermarkSeconds == null) ? String.format("%d microseconds", watermarkMicroseconds) : String.format("%d seconds", watermarkSeconds);
			
			dataset = dataset
				.withColumn("group_all", functions.lit(0))
				.withWatermark("timestamp", watermarkString)
				.groupBy("group_all", "timestamp")
				.agg(functions.countDistinct(colName))
				.select(col("countDistinct(" + colName + ")"));
			break;
		}
		
		datasetGrouped = DatasetGrouped.UNGROUPED;
		return this;
	}
	
	public FeaturePipeline first(String colName) {
		switch(datasetGrouped) {
		case TIME:
			dataset = relationalDataset
				.agg(functions.first(colName))
				.select(col("window"), col("first(" + colName + ")"))
				.withColumn("window_start", col("window").getField("start"))
				.withColumn("window_end", col("window").getField("end"))
				.drop(col("window"));
			break;
	
		case KEYVAL:
			dataset = relationalDataset
				.agg(functions.first(colName))
				.select(col("group"), col("first(" + colName + ")"));
			break;
			
		case UNGROUPED:
			throw new IllegalArgumentException("Watermark needs to be specified when dataset is not grouped");
		}
		
		datasetGrouped = DatasetGrouped.UNGROUPED;
		return this;
	}
		
	public FeaturePipeline first(String colName, Integer watermarkSeconds, Integer watermarkMicroseconds){
		switch(datasetGrouped) {
		case TIME:
			throw new IllegalArgumentException("Watermark cant be respecified after grouping");
		case KEYVAL:
			throw new IllegalArgumentException("Watermark cant be respecified after grouping");
		case UNGROUPED:
			assert watermarkSeconds == null || watermarkMicroseconds == null: "Only Seconds OR Microseconds can be specified for Watermark";
			assert watermarkSeconds != null || watermarkMicroseconds != null: "Either Seconds OR Microseconds mmust be specified for Watermark";
			
			String watermarkString = (watermarkSeconds == null) ? String.format("%d microseconds", watermarkMicroseconds) : String.format("%d seconds", watermarkSeconds);
			
			dataset = dataset
				.withColumn("group_all", functions.lit(0))
				.withWatermark("timestamp", watermarkString)
				.groupBy("group_all", "timestamp")
				.agg(functions.first(colName))
				.select(col("first(" + colName + ")"));
			break;
		}
		
		datasetGrouped = DatasetGrouped.UNGROUPED;
		return this;
	}
	
	public FeaturePipeline kurtosis(String colName) {
		switch(datasetGrouped) {
		case TIME:
			dataset = relationalDataset
				.agg(functions.kurtosis(colName))
				.select(col("window"), col("kurtosis(" + colName + ")"))
				.withColumn("window_start", col("window").getField("start"))
				.withColumn("window_end", col("window").getField("end"))
				.drop(col("window"));
			break;
	
		case KEYVAL:
			dataset = relationalDataset
				.agg(functions.kurtosis(colName))
				.select(col("group"), col("kurtosis(" + colName + ")"));
			break;
			
		case UNGROUPED:
			throw new IllegalArgumentException("Watermark needs to be specified when dataset is not grouped");
		}
		
		datasetGrouped = DatasetGrouped.UNGROUPED;
		return this;
	}
		
	public FeaturePipeline kurtosis(String colName, Integer watermarkSeconds, Integer watermarkMicroseconds){
		switch(datasetGrouped) {
		case TIME:
			throw new IllegalArgumentException("Watermark cant be respecified after grouping");
		case KEYVAL:
			throw new IllegalArgumentException("Watermark cant be respecified after grouping");
		case UNGROUPED:
			assert watermarkSeconds == null || watermarkMicroseconds == null: "Only Seconds OR Microseconds can be specified for Watermark";
			assert watermarkSeconds != null || watermarkMicroseconds != null: "Either Seconds OR Microseconds mmust be specified for Watermark";
			
			String watermarkString = (watermarkSeconds == null) ? String.format("%d microseconds", watermarkMicroseconds) : String.format("%d seconds", watermarkSeconds);
			
			dataset = dataset
				.withColumn("group_all", functions.lit(0))
				.withWatermark("timestamp", watermarkString)
				.groupBy("group_all", "timestamp")
				.agg(functions.kurtosis(colName))
				.select(col("kurtosis(" + colName + ")"));
			break;
		}
		
		datasetGrouped = DatasetGrouped.UNGROUPED;
		return this;
	}
	
	public FeaturePipeline last(String colName) {
		switch(datasetGrouped) {
		case TIME:
			dataset = relationalDataset
				.agg(functions.last(colName))
				.select(col("window"), col("last(" + colName + ")"))
				.withColumn("window_start", col("window").getField("start"))
				.withColumn("window_end", col("window").getField("end"))
				.drop(col("window"));
			break;
	
		case KEYVAL:
			dataset = relationalDataset
				.agg(functions.last(colName))
				.select(col("group"), col("last(" + colName + ")"));
			break;
			
		case UNGROUPED:
			throw new IllegalArgumentException("Watermark needs to be specified when dataset is not grouped");
		}
		
		datasetGrouped = DatasetGrouped.UNGROUPED;
		return this;
	}
		
	public FeaturePipeline last(String colName, Integer watermarkSeconds, Integer watermarkMicroseconds){
		switch(datasetGrouped) {
		case TIME:
			throw new IllegalArgumentException("Watermark cant be respecified after grouping");
		case KEYVAL:
			throw new IllegalArgumentException("Watermark cant be respecified after grouping");
		case UNGROUPED:
			assert watermarkSeconds == null || watermarkMicroseconds == null: "Only Seconds OR Microseconds can be specified for Watermark";
			assert watermarkSeconds != null || watermarkMicroseconds != null: "Either Seconds OR Microseconds mmust be specified for Watermark";
			
			String watermarkString = (watermarkSeconds == null) ? String.format("%d microseconds", watermarkMicroseconds) : String.format("%d seconds", watermarkSeconds);
			
			dataset = dataset
				.withColumn("group_all", functions.lit(0))
				.withWatermark("timestamp", watermarkString)
				.groupBy("group_all", "timestamp")
				.agg(functions.last(colName))
				.select(col("last(" + colName + ")"));
			break;
		}
		
		datasetGrouped = DatasetGrouped.UNGROUPED;
		return this;
	}
	
	public FeaturePipeline max(String colName) {
		switch(datasetGrouped) {
		case TIME:
			dataset = relationalDataset
				.agg(functions.max(colName))
				.select(col("window"), col("max(" + colName + ")"))
				.withColumn("window_start", col("window").getField("start"))
				.withColumn("window_end", col("window").getField("end"))
				.drop(col("window"));
			break;
	
		case KEYVAL:
			dataset = relationalDataset
				.agg(functions.max(colName))
				.select(col("group"), col("max(" + colName + ")"));
			break;
			
		case UNGROUPED:
			throw new IllegalArgumentException("Watermark needs to be specified when dataset is not grouped");
		}
		
		datasetGrouped = DatasetGrouped.UNGROUPED;
		return this;
	}
		
	public FeaturePipeline max(String colName, Integer watermarkSeconds, Integer watermarkMicroseconds){
		switch(datasetGrouped) {
		case TIME:
			throw new IllegalArgumentException("Watermark cant be respecified after grouping");
		case KEYVAL:
			throw new IllegalArgumentException("Watermark cant be respecified after grouping");
		case UNGROUPED:
			assert watermarkSeconds == null || watermarkMicroseconds == null: "Only Seconds OR Microseconds can be specified for Watermark";
			assert watermarkSeconds != null || watermarkMicroseconds != null: "Either Seconds OR Microseconds mmust be specified for Watermark";
			
			String watermarkString = (watermarkSeconds == null) ? String.format("%d microseconds", watermarkMicroseconds) : String.format("%d seconds", watermarkSeconds);
			
			dataset = dataset
				.withColumn("group_all", functions.lit(0))
				.withWatermark("timestamp", watermarkString)
				.groupBy("group_all", "timestamp")
				.agg(functions.max(colName))
				.select(col("max(" + colName + ")"));
			break;
		}
		
		datasetGrouped = DatasetGrouped.UNGROUPED;
		return this;
	}
	
	public FeaturePipeline mean(String colName) {
		switch(datasetGrouped) {
		case TIME:
			dataset = relationalDataset
				.agg(functions.mean(colName))
				.select(col("window"), col("mean(" + colName + ")"))
				.withColumn("window_start", col("window").getField("start"))
				.withColumn("window_end", col("window").getField("end"))
				.drop(col("window"));
			break;
	
		case KEYVAL:
			dataset = relationalDataset
				.agg(functions.mean(colName))
				.select(col("group"), col("mean(" + colName + ")"));
			break;
			
		case UNGROUPED:
			throw new IllegalArgumentException("Watermark needs to be specified when dataset is not grouped");
		}
		
		datasetGrouped = DatasetGrouped.UNGROUPED;
		return this;
	}
		
	public FeaturePipeline mean(String colName, Integer watermarkSeconds, Integer watermarkMicroseconds){
		switch(datasetGrouped) {
		case TIME:
			throw new IllegalArgumentException("Watermark cant be respecified after grouping");
		case KEYVAL:
			throw new IllegalArgumentException("Watermark cant be respecified after grouping");
		case UNGROUPED:
			assert watermarkSeconds == null || watermarkMicroseconds == null: "Only Seconds OR Microseconds can be specified for Watermark";
			assert watermarkSeconds != null || watermarkMicroseconds != null: "Either Seconds OR Microseconds mmust be specified for Watermark";
			
			String watermarkString = (watermarkSeconds == null) ? String.format("%d microseconds", watermarkMicroseconds) : String.format("%d seconds", watermarkSeconds);
			
			dataset = dataset
				.withColumn("group_all", functions.lit(0))
				.withWatermark("timestamp", watermarkString)
				.groupBy("group_all", "timestamp")
				.agg(functions.mean(colName))
				.select(col("mean(" + colName + ")"));
			break;
		}
		
		datasetGrouped = DatasetGrouped.UNGROUPED;
		return this;
	}
	
	
	public FeaturePipeline median(String colName) {
		switch(datasetGrouped) {
		case TIME:
			dataset = relationalDataset
				.agg(functions.median(col(colName)))
				.select(col("window"), col("median(" + colName + ")"))
				.withColumn("window_start", col("window").getField("start"))
				.withColumn("window_end", col("window").getField("end"))
				.drop(col("window"));
			break;
	
		case KEYVAL:
			dataset = relationalDataset
				.agg(functions.median(col(colName)))
				.select(col("group"), col("median(" + colName + ")"));
			break;
			
		case UNGROUPED:
			throw new IllegalArgumentException("Watermark needs to be specified when dataset is not grouped");
		}
		
		datasetGrouped = DatasetGrouped.UNGROUPED;
		return this;
	}
		
	public FeaturePipeline median(String colName, Integer watermarkSeconds, Integer watermarkMicroseconds){
		switch(datasetGrouped) {
		case TIME:
			throw new IllegalArgumentException("Watermark cant be respecified after grouping");
		case KEYVAL:
			throw new IllegalArgumentException("Watermark cant be respecified after grouping");
		case UNGROUPED:
			assert watermarkSeconds == null || watermarkMicroseconds == null: "Only Seconds OR Microseconds can be specified for Watermark";
			assert watermarkSeconds != null || watermarkMicroseconds != null: "Either Seconds OR Microseconds mmust be specified for Watermark";
			
			String watermarkString = (watermarkSeconds == null) ? String.format("%d microseconds", watermarkMicroseconds) : String.format("%d seconds", watermarkSeconds);
			
			dataset = dataset
				.withColumn("group_all", functions.lit(0))
				.withWatermark("timestamp", watermarkString)
				.groupBy("group_all", "timestamp")
				.agg(functions.median(col(colName)))
				.select(col("median(" + colName + ")"));
			break;
		}
		
		datasetGrouped = DatasetGrouped.UNGROUPED;
		return this;
	}
	
	public FeaturePipeline min(String colName) {
		switch(datasetGrouped) {
		case TIME:
			dataset = relationalDataset
				.agg(functions.min(colName))
				.select(col("window"), col("min(" + colName + ")"))
				.withColumn("window_start", col("window").getField("start"))
				.withColumn("window_end", col("window").getField("end"))
				.drop(col("window"));
			break;
	
		case KEYVAL:
			dataset = relationalDataset
				.agg(functions.min(colName))
				.select(col("group"), col("min(" + colName + ")"));
			break;
			
		case UNGROUPED:
			throw new IllegalArgumentException("Watermark needs to be specified when dataset is not grouped");
		}
		
		datasetGrouped = DatasetGrouped.UNGROUPED;
		return this;
	}
		
	public FeaturePipeline min(String colName, Integer watermarkSeconds, Integer watermarkMicroseconds){
		switch(datasetGrouped) {
		case TIME:
			throw new IllegalArgumentException("Watermark cant be respecified after grouping");
		case KEYVAL:
			throw new IllegalArgumentException("Watermark cant be respecified after grouping");
		case UNGROUPED:
			assert watermarkSeconds == null || watermarkMicroseconds == null: "Only Seconds OR Microseconds can be specified for Watermark";
			assert watermarkSeconds != null || watermarkMicroseconds != null: "Either Seconds OR Microseconds mmust be specified for Watermark";
			
			String watermarkString = (watermarkSeconds == null) ? String.format("%d microseconds", watermarkMicroseconds) : String.format("%d seconds", watermarkSeconds);
			
			dataset = dataset
				.withColumn("group_all", functions.lit(0))
				.withWatermark("timestamp", watermarkString)
				.groupBy("group_all", "timestamp")
				.agg(functions.min(colName))
				.select(col("min(" + colName + ")"));
			break;
		}
		
		datasetGrouped = DatasetGrouped.UNGROUPED;
		return this;
	}
	
	public FeaturePipeline mode(String colName) {
		switch(datasetGrouped) {
		case TIME:
			dataset = relationalDataset
				.agg(functions.mode(col(colName)))
				.select(col("window"), col("mode(" + colName + ")"))
				.withColumn("window_start", col("window").getField("start"))
				.withColumn("window_end", col("window").getField("end"))
				.drop(col("window"));
			break;
	
		case KEYVAL:
			dataset = relationalDataset
				.agg(functions.mode(col(colName)))
				.select(col("group"), col("mode(" + colName + ")"));
			break;
			
		case UNGROUPED:
			throw new IllegalArgumentException("Watermark needs to be specified when dataset is not grouped");
		}
		
		datasetGrouped = DatasetGrouped.UNGROUPED;
		return this;
	}
		
	public FeaturePipeline mode(String colName, Integer watermarkSeconds, Integer watermarkMicroseconds){
		switch(datasetGrouped) {
		case TIME:
			throw new IllegalArgumentException("Watermark cant be respecified after grouping");
		case KEYVAL:
			throw new IllegalArgumentException("Watermark cant be respecified after grouping");
		case UNGROUPED:
			assert watermarkSeconds == null || watermarkMicroseconds == null: "Only Seconds OR Microseconds can be specified for Watermark";
			assert watermarkSeconds != null || watermarkMicroseconds != null: "Either Seconds OR Microseconds mmust be specified for Watermark";
			
			String watermarkString = (watermarkSeconds == null) ? String.format("%d microseconds", watermarkMicroseconds) : String.format("%d seconds", watermarkSeconds);
			
			dataset = dataset
				.withColumn("group_all", functions.lit(0))
				.withWatermark("timestamp", watermarkString)
				.groupBy("group_all", "timestamp")
				.agg(functions.mode(col(colName)))
				.select(col("mode(" + colName + ")"));
			break;
		}
		
		datasetGrouped = DatasetGrouped.UNGROUPED;
		return this;
	}
	
	public FeaturePipeline product(String colName) {
		switch(datasetGrouped) {
		case TIME:
			dataset = relationalDataset
				.agg(functions.product(col(colName)))
				.select(col("window"), col("product(" + colName + ")"))
				.withColumn("window_start", col("window").getField("start"))
				.withColumn("window_end", col("window").getField("end"))
				.drop(col("window"));
			break;
	
		case KEYVAL:
			dataset = relationalDataset
				.agg(functions.product(col(colName)))
				.select(col("group"), col("product(" + colName + ")"));
			break;
			
		case UNGROUPED:
			throw new IllegalArgumentException("Watermark needs to be specified when dataset is not grouped");
		}
		
		datasetGrouped = DatasetGrouped.UNGROUPED;
		return this;
	}
		
	public FeaturePipeline product(String colName, Integer watermarkSeconds, Integer watermarkMicroseconds){
		switch(datasetGrouped) {
		case TIME:
			throw new IllegalArgumentException("Watermark cant be respecified after grouping");
		case KEYVAL:
			throw new IllegalArgumentException("Watermark cant be respecified after grouping");
		case UNGROUPED:
			assert watermarkSeconds == null || watermarkMicroseconds == null: "Only Seconds OR Microseconds can be specified for Watermark";
			assert watermarkSeconds != null || watermarkMicroseconds != null: "Either Seconds OR Microseconds mmust be specified for Watermark";
			
			String watermarkString = (watermarkSeconds == null) ? String.format("%d microseconds", watermarkMicroseconds) : String.format("%d seconds", watermarkSeconds);
			
			dataset = dataset
				.withColumn("group_all", functions.lit(0))
				.withWatermark("timestamp", watermarkString)
				.groupBy("group_all", "timestamp")
				.agg(functions.product(col(colName)))
				.select(col("product(" + colName + ")"));
			break;
		}
		
		datasetGrouped = DatasetGrouped.UNGROUPED;
		return this;
	}
	
	public FeaturePipeline skewness(String colName) {
		switch(datasetGrouped) {
		case TIME:
			dataset = relationalDataset
				.agg(functions.skewness(colName))
				.select(col("window"), col("skewness(" + colName + ")"))
				.withColumn("window_start", col("window").getField("start"))
				.withColumn("window_end", col("window").getField("end"))
				.drop(col("window"));
			break;
	
		case KEYVAL:
			dataset = relationalDataset
				.agg(functions.skewness(colName))
				.select(col("group"), col("skewness(" + colName + ")"));
			break;
			
		case UNGROUPED:
			throw new IllegalArgumentException("Watermark needs to be specified when dataset is not grouped");
		}
		
		datasetGrouped = DatasetGrouped.UNGROUPED;
		return this;
	}
		
	public FeaturePipeline skewness(String colName, Integer watermarkSeconds, Integer watermarkMicroseconds){
		switch(datasetGrouped) {
		case TIME:
			throw new IllegalArgumentException("Watermark cant be respecified after grouping");
		case KEYVAL:
			throw new IllegalArgumentException("Watermark cant be respecified after grouping");
		case UNGROUPED:
			assert watermarkSeconds == null || watermarkMicroseconds == null: "Only Seconds OR Microseconds can be specified for Watermark";
			assert watermarkSeconds != null || watermarkMicroseconds != null: "Either Seconds OR Microseconds mmust be specified for Watermark";
			
			String watermarkString = (watermarkSeconds == null) ? String.format("%d microseconds", watermarkMicroseconds) : String.format("%d seconds", watermarkSeconds);
			
			dataset = dataset
				.withColumn("group_all", functions.lit(0))
				.withWatermark("timestamp", watermarkString)
				.groupBy("group_all", "timestamp")
				.agg(functions.skewness(colName))
				.select(col("skewness(" + colName + ")"));
			break;
		}
		
		datasetGrouped = DatasetGrouped.UNGROUPED;
		return this;
	}
	
	public FeaturePipeline some(String colName) {
		switch(datasetGrouped) {
		case TIME:
			dataset = relationalDataset
				.agg(functions.some(col(colName)))
				.select(col("window"), col("some(" + colName + ")"))
				.withColumn("window_start", col("window").getField("start"))
				.withColumn("window_end", col("window").getField("end"))
				.drop(col("window"));
			break;
	
		case KEYVAL:
			dataset = relationalDataset
				.agg(functions.some(col(colName)))
				.select(col("group"), col("some(" + colName + ")"));
			break;
			
		case UNGROUPED:
			throw new IllegalArgumentException("Watermark needs to be specified when dataset is not grouped");
		}
		
		datasetGrouped = DatasetGrouped.UNGROUPED;
		return this;
	}
		
	public FeaturePipeline some(String colName, Integer watermarkSeconds, Integer watermarkMicroseconds){
		switch(datasetGrouped) {
		case TIME:
			throw new IllegalArgumentException("Watermark cant be respecified after grouping");
		case KEYVAL:
			throw new IllegalArgumentException("Watermark cant be respecified after grouping");
		case UNGROUPED:
			assert watermarkSeconds == null || watermarkMicroseconds == null: "Only Seconds OR Microseconds can be specified for Watermark";
			assert watermarkSeconds != null || watermarkMicroseconds != null: "Either Seconds OR Microseconds mmust be specified for Watermark";
			
			String watermarkString = (watermarkSeconds == null) ? String.format("%d microseconds", watermarkMicroseconds) : String.format("%d seconds", watermarkSeconds);
			
			dataset = dataset
				.withColumn("group_all", functions.lit(0))
				.withWatermark("timestamp", watermarkString)
				.groupBy("group_all", "timestamp")
				.agg(functions.some(col(colName)))
				.select(col("some(" + colName + ")"));
			break;
		}
		
		datasetGrouped = DatasetGrouped.UNGROUPED;
		return this;
	}
	
	public FeaturePipeline std(String colName) {
		switch(datasetGrouped) {
		case TIME:
			dataset = relationalDataset
				.agg(functions.std(col(colName)))
				.select(col("window"), col("std(" + colName + ")"))
				.withColumn("window_start", col("window").getField("start"))
				.withColumn("window_end", col("window").getField("end"))
				.drop(col("window"));
			break;
	
		case KEYVAL:
			dataset = relationalDataset
				.agg(functions.std(col(colName)))
				.select(col("group"), col("std(" + colName + ")"));
			break;
			
		case UNGROUPED:
			throw new IllegalArgumentException("Watermark needs to be specified when dataset is not grouped");
		}
		
		datasetGrouped = DatasetGrouped.UNGROUPED;
		return this;
	}
		
	public FeaturePipeline std(String colName, Integer watermarkSeconds, Integer watermarkMicroseconds){
		switch(datasetGrouped) {
		case TIME:
			throw new IllegalArgumentException("Watermark cant be respecified after grouping");
		case KEYVAL:
			throw new IllegalArgumentException("Watermark cant be respecified after grouping");
		case UNGROUPED:
			assert watermarkSeconds == null || watermarkMicroseconds == null: "Only Seconds OR Microseconds can be specified for Watermark";
			assert watermarkSeconds != null || watermarkMicroseconds != null: "Either Seconds OR Microseconds mmust be specified for Watermark";
			
			String watermarkString = (watermarkSeconds == null) ? String.format("%d microseconds", watermarkMicroseconds) : String.format("%d seconds", watermarkSeconds);
			
			dataset = dataset
				.withColumn("group_all", functions.lit(0))
				.withWatermark("timestamp", watermarkString)
				.groupBy("group_all", "timestamp")
				.agg(functions.std(col(colName)))
				.select(col("std(" + colName + ")"));
			break;
		}
		
		datasetGrouped = DatasetGrouped.UNGROUPED;
		return this;
	}
	
	public FeaturePipeline sum(String colName) {
		switch(datasetGrouped) {
		case TIME:
			dataset = relationalDataset
				.agg(functions.sum(colName))
				.select(col("window"), col("sum(" + colName + ")"))
				.withColumn("window_start", col("window").getField("start"))
				.withColumn("window_end", col("window").getField("end"))
				.drop(col("window"));
			break;
	
		case KEYVAL:
			dataset = relationalDataset
				.agg(functions.sum(colName))
				.select(col("group"), col("sum(" + colName + ")"));
			break;
			
		case UNGROUPED:
			throw new IllegalArgumentException("Watermark needs to be specified when dataset is not grouped");
		}
		
		datasetGrouped = DatasetGrouped.UNGROUPED;
		return this;
	}
		
	public FeaturePipeline sum(String colName, Integer watermarkSeconds, Integer watermarkMicroseconds){
		switch(datasetGrouped) {
		case TIME:
			throw new IllegalArgumentException("Watermark cant be respecified after grouping");
		case KEYVAL:
			throw new IllegalArgumentException("Watermark cant be respecified after grouping");
		case UNGROUPED:
			assert watermarkSeconds == null || watermarkMicroseconds == null: "Only Seconds OR Microseconds can be specified for Watermark";
			assert watermarkSeconds != null || watermarkMicroseconds != null: "Either Seconds OR Microseconds mmust be specified for Watermark";
			
			String watermarkString = (watermarkSeconds == null) ? String.format("%d microseconds", watermarkMicroseconds) : String.format("%d seconds", watermarkSeconds);
			
			dataset = dataset
				.withColumn("group_all", functions.lit(0))
				.withWatermark("timestamp", watermarkString)
				.groupBy("group_all", "timestamp")
				.agg(functions.sum(colName))
				.select(col("sum(" + colName + ")"));
			break;
		}
		
		datasetGrouped = DatasetGrouped.UNGROUPED;
		return this;
	}
	

	/*
	 * Multicol functions
	 */
	
	
	public FeaturePipeline concat(String newColName, Boolean keep, String... colNames) {
		Column[] cols = new Column[colNames.length];
		for(int i = 0; i < colNames.length; i++ ) {
			cols[i] = col(colNames[i]);
		}
		
		dataset = dataset
				.withColumn(newColName, functions.concat(cols));
		
		if(!keep) {
			dataset = dataset.drop(colNames);
		}
		return this;
	}
	
	public FeaturePipeline contains(String right, String left, String newColName, Boolean keep)  {
		
		dataset = dataset
				.withColumn(newColName, functions.contains(col(left), col(right)));
		
		if(!keep) {
			dataset = dataset
					.drop(right)
					.drop(left);
		}
		return this;
	}
	
	public FeaturePipeline corr(String right, String left, String newColName, Boolean keep)  {
		
		dataset = dataset
				.withColumn(newColName, functions.corr(col(left), col(right)));
		
		if(!keep) {
			dataset = dataset
					.drop(right)
					.drop(left);
		}
		return this;
	}
	
	public FeaturePipeline date_diff(String right, String left, String newColName, Boolean keep)  {
		
		dataset = dataset
				.withColumn(newColName, functions.date_diff(col(left), col(right)));
		
		if(!keep) {
			dataset = dataset
					.drop(right)
					.drop(left);
		}
		return this;
	}
	
	public FeaturePipeline make_date(String year, String month, String day, String newColName, Boolean keep)  {
		
		dataset = dataset
				.withColumn(newColName, functions.make_date(col(year), col(month), col(day)));
		
		if(!keep) {
			dataset = dataset
					.drop(year)
					.drop(month)
					.drop(day);
		}
		
		return this;	
	}
	
	public FeaturePipeline try_divide(String dividend, String divisor, String newColName, Boolean keep)  {
		
		dataset = dataset
				.withColumn(newColName, functions.try_divide(col(dividend), col(divisor)));
		
		if(!keep) {
			dataset = dataset
					.drop(dividend)
					.drop(divisor);
		}
		
		return this;
		
	}
	

	
	/*
	 * Signal-processing functions
	*/
	
	public FeaturePipeline resample(String colName, Double srate_new){
		FlatMapGroupsWithStateFunction<Timestamp, Row, SamplingStatus, Row> resampleFun = new FlatMapGroupsWithStateFunction<>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Iterator<Row> call(Timestamp key, Iterator<Row> values,
					GroupState<SamplingStatus> state) throws Exception {
				
				ArrayList<Row> result = new ArrayList<>();
				
				Long newX = key.getTime();
				Integer ix = dataset.schema().fieldIndex(colName);
				Double newY = values.next().getDouble(ix);
				
				if(state.exists()){
				
					Double prevX = state.get().getPoint()[0];
					Double prevY = state.get().getPoint()[1];			
					
					int i = 1;
					
					while(prevX + i * 1 / srate_new * 1000 < newX) {
						Double interpolatedX = prevX + i * 1 / srate_new ;
						Row newPoint = RowFactory.create(
								new Timestamp( Math.round( interpolatedX * 1000)),
								prevY + (newY - prevY) / (newX - prevX) * (interpolatedX - prevX)
								);
						result.add(newPoint);
						i++;
					}
				} else {
					Row newPoint = RowFactory.create(
							new Timestamp( newX),
							newY
							);
					result.add(newPoint);
				}
					
				SamplingStatus newState = new SamplingStatus();
				newState.setPoint(new Double[] {(double) newX, newY} );
				state.update(newState);
				
				return result.iterator();
			}
		};
		MapFunction<Row, Timestamp> grouping = groupTime();
		
		dataset = dataset
				.groupByKey(grouping, Encoders.TIMESTAMP())
				.flatMapGroupsWithState(resampleFun, OutputMode.Append(), Encoders.bean(SamplingStatus.class), Encoders.row(dataset.schema()), GroupStateTimeout.NoTimeout());
		
		return this;
	}
	
	
	public FeaturePipeline bandpassFilter(String colName, Double samplingrate, Double lowerCutoff, Double upperCutoff, Integer order) {
		FeaturePipeline.filter = new IIR_Filters(samplingrate).registerBandpass(order, lowerCutoff, upperCutoff);
		MapFunction<Row, Row> bpFilter = new MapFunction<Row, Row>(){
			
			private static final long serialVersionUID = 1L;		
			
			@Override
			public Row call(Row val) throws Exception {
				
				Object[] newRow = new Object[val.length()+1];				
				for(int i = 0; i < val.length(); i++) {
					newRow[i] = val.get(i);
				}
				int ix = val.fieldIndex(colName);
				newRow[val.length()] = filter.filter(val.getDouble(ix));
						
				return RowFactory.create(newRow);
			}			
		};
		
		StructType schema_new = dataset.schema().add(new StructField(colName + "_filt", DataTypes.DoubleType, true, Metadata.empty()));
		dataset = dataset
				.map(bpFilter, Encoders.row(schema_new));
		
		return this;
	}
	
	
	
	/*
	 * Window functions
	 */

	
	/*
	 * Lag functions
	 */
	
	
	public FeaturePipeline lag(String colName, Double fillBack){
		
		FlatMapGroupsWithStateFunction<Timestamp, Row, FeatureStatus, Row> lagFun = new FlatMapGroupsWithStateFunction<Timestamp, Row, FeatureStatus, Row>(){
							
				private static final long serialVersionUID = 1L;
		
				@Override
				public Iterator<Row> call(Timestamp key, Iterator<Row> values,
						GroupState<FeatureStatus> state) throws Exception {
									
					ArrayList<Row> result = new ArrayList<>();
					
					Row current = values.next();				
							
					Object[] newRow = new Object[current.length()+1];				
					for(int i = 0; i < current.length(); i++) {
						newRow[i] = current.get(i);
					}
					
					int ix = current.fieldIndex(colName);
					FeatureStatus newStatus = new FeatureStatus();
					newStatus.setStatus(current.getDouble(ix));
					
					if(state.exists()){
						newRow[current.length()] = state.get().getStatus();
					} else {
						System.out.println("SState does not exist");
						newRow[current.length()] = fillBack;
					}
					
					state.update(newStatus);
					result.add(RowFactory.create(newRow));
					return result.iterator();
				}	
			};
			
		MapFunction<Row, Timestamp> grouping = groupTime();
		
		StructType schema_new = dataset.schema().add(new StructField(colName + "_lag", DataTypes.DoubleType, true, Metadata.empty()));
		dataset = dataset
				.groupByKey(grouping, Encoders.TIMESTAMP())
				.flatMapGroupsWithState(lagFun, OutputMode.Append(), Encoders.bean(FeatureStatus.class), Encoders.row(schema_new), GroupStateTimeout.NoTimeout());			
	
		return this;
	}
	 
	
	public FeaturePipeline diff(String colName, Double fillBack){
		
		FlatMapGroupsWithStateFunction<Timestamp, Row, FeatureStatus, Row> diffFun = new FlatMapGroupsWithStateFunction<Timestamp, Row, FeatureStatus, Row>(){
							
				private static final long serialVersionUID = 1L;
		
				@Override
				public Iterator<Row> call(Timestamp key, Iterator<Row> values, GroupState<FeatureStatus> state) throws Exception {
									
					ArrayList<Row> result = new ArrayList<>();
					
					
					Row current = values.next();
					
					Object[] newRow = new Object[current.length()+1];				
					for(int i = 0; i < current.length(); i++) {
						newRow[i] = current.get(i);
					}
					
					int ix = current.fieldIndex(colName);
					FeatureStatus newStatus = new FeatureStatus();
					newStatus.setStatus(current.getDouble(ix));
					
					if(state.exists()){
						System.out.println("SState seems to exist");
						newRow[current.length()] = current.getDouble(ix) - state.get().getStatus();
					} else {
						System.out.println("SState does not exist");
						newRow[current.length()] = fillBack;
					}
					
					state.update(newStatus);
					result.add(RowFactory.create(newRow));
					return result.iterator();
				}	
			};
			
		MapFunction<Row, Timestamp> grouping = groupTime();
		
		StructType schema_new = dataset.schema().add(new StructField(colName + "_diff", DataTypes.DoubleType, true, Metadata.empty()));
		dataset = dataset
				.groupByKey(grouping, Encoders.TIMESTAMP())
				.flatMapGroupsWithState(diffFun, OutputMode.Append(), Encoders.bean(FeatureStatus.class), Encoders.row(schema_new), GroupStateTimeout.NoTimeout());			
	
		return this;
	}
	
		
	
	/*
	 * Helper Functions
	 */
	
	
	public FeaturePipeline select(String...colnames ) {
		dataset = dataset
				.select("timestamp", colnames);
		return this;
	}
	
	public FeaturePipeline rename(String oldColName, String newColName ) {
		dataset = dataset
				.withColumnRenamed(oldColName, newColName);
		return this;
	}
	
	public FeaturePipeline joinPipelines(FeaturePipeline pipeline2, String timeColName1, String timeColName2, String watermarkString1, String watermarkString2, String joinTimeWindow, String jointype ) {
		String joinexpr = String.format("%s = %s AND %s >= %s AND %s <= %s + interval %s", timeColName2, timeColName1, timeColName2, timeColName1, timeColName2, timeColName1, joinTimeWindow);
		String[] possibleJoinTypes = {"inner", "leftOuter", "rightOuter", "fullOuter", "leftSemi"};
		assert Arrays.stream(possibleJoinTypes).anyMatch(jointype::equals): "joinType must be one of " + possibleJoinTypes.toString();
		
		dataset = dataset
				.withWatermark(timeColName1, watermarkString1)
				.join(pipeline2.getDataset().withWatermark(timeColName2, watermarkString2),
					functions.expr(joinexpr),
					jointype);
		
		return this;
	}
	
	
}
