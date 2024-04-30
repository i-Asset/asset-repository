package at.srfg.iasset.connector.featureStore;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.apache.spark.api.java.function.FlatMapGroupsWithStateFunction;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.streaming.GroupState;
import org.apache.spark.sql.streaming.GroupStateTimeout;
import org.apache.spark.sql.streaming.OutputMode;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import at.srfg.iasset.connector.featureStore.JavaStructuredSessionization.MartingaleStatus;

public class FeatureMonitoring implements Serializable{

	private static final long serialVersionUID = 1L;
	private Dataset<Row> dataset;
	private Double lambda;
	private StructType returnSchema = new StructType(new StructField[] {
		new StructField("strangenessValue", DataTypes.DoubleType, true, Metadata.empty()),
		new StructField("martingaleValue", DataTypes.DoubleType, true, Metadata.empty()),
		new StructField("changeDetected", DataTypes.BooleanType, true, Metadata.empty()) 
	});
	
	public FeatureMonitoring(Dataset<Row> dataset, Double lambda) {
		this.dataset = dataset;
		this.lambda = lambda;
	}
	
	private MapFunction<Row, Timestamp> groupIndividual(){
		
		return new MapFunction<Row, Timestamp>(){
	
			private static final long serialVersionUID = 1L;
	
			@Override
			public Timestamp call(Row arg0) throws Exception {
				int ix = arg0.fieldIndex("timestamp");
				return arg0.getTimestamp(ix);
			}
		};
	}
	
	public Dataset<Row> martingaleTest(String featurenName, Double eta){
		FlatMapGroupsWithStateFunction<Timestamp, Row, MartingaleStatus, Row> martingaleTestFun = new FlatMapGroupsWithStateFunction<>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Iterator<Row> call(Timestamp key, Iterator<Row> values, GroupState<MartingaleStatus> state) throws Exception {
				
				ArrayList<Row> result = new ArrayList<>();
				Row s = values.next();
				Double mI;
				Integer ixFeatures = s.fieldIndex(featurenName);
				Boolean changeDetected = false;
				Double observedStrangeness = s.getDouble(ixFeatures);
				
				if(state.exists()){
					
					MartingaleStatus stateI = state.get();
					stateI.addS(observedStrangeness);
					state.update(stateI);	
					if(stateI.sizeT() == 1) {
						mI = 1D;
					} else {
						ArrayList<Double> ps = new ArrayList<>();
						for (Map.Entry<Double, Integer> entry : stateI.getT().entrySet()) {
							ps.add(stateI.GreaterThanS(entry.getKey()) / Double.valueOf(entry.getValue()));
					    }
						mI = ps
							.stream()
							.reduce(1D,  (subtotal, element) -> subtotal * (eta * Math.pow(element, eta-1D)));
						changeDetected = mI >= lambda ? true: false;
						if(changeDetected) {
							stateI.resetT();
							state.update(stateI);
						}
					}
				} else {
					mI = 1D;
					MartingaleStatus stateI = new MartingaleStatus();
 					stateI.addS(observedStrangeness);
					state.update(stateI);
				}
				Row newPoint = RowFactory.create(observedStrangeness, mI, changeDetected);
				result.add(newPoint);
				return result.iterator();
			}
		};
		MapFunction<Row, Timestamp> grouping = groupIndividual();
		
		dataset = dataset
				.groupByKey(grouping, Encoders.TIMESTAMP())
				.flatMapGroupsWithState(martingaleTestFun, OutputMode.Append(), Encoders.bean(MartingaleStatus.class), Encoders.row(returnSchema), GroupStateTimeout.NoTimeout());
		
		return dataset;
	}

}
