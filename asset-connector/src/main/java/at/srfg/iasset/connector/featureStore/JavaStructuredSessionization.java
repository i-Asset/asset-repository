package at.srfg.iasset.connector.featureStore;

import java.beans.JavaBean;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.SortedMap;
import java.util.TreeMap;

public final class JavaStructuredSessionization {	
	
	@SuppressWarnings("serial")
	@JavaBean
    public static class LineWithTimestamp implements Serializable {
        private Timestamp timestamp;
        private Double line;
        
		public Timestamp getTimestamp() {
			return timestamp;
		}
		public void setTimestamp(Timestamp timestamp) {
			this.timestamp = timestamp;
		}
		public Double getLine() {
			return line;
		}
		public void setLine(Double line) {
			this.line = line;
		}
        
    }
	
    @SuppressWarnings("serial")
	@JavaBean
    public static class FeatureStatus implements Serializable{
    	private Double status;
    	
		public Double getStatus() {
			return status;
		}

		public void setStatus(Double status) {
			this.status = status;
		}
    	
    }
    
    @SuppressWarnings("serial")
	@JavaBean
    public static class SamplingStatus implements Serializable{
    	private Double[] point;
    	
		public Double[] getPoint() {
			return point;
		}

		public void setPoint(Double[] point) {
			this.point = point;
		}
    	
    }
    
    @SuppressWarnings("serial")
	@JavaBean
    public static class MartingaleStatus implements Serializable{
    	private TreeMap<Double, Integer> T = new TreeMap<>();
    	private Double tol = 1e-6;
    	private Integer i = 0;
    	
    	public void addS(Double s) {
    		while(T.containsKey(s)) {
    			s += tol;
    		}
    		T.put(s, i++);
    	}
    	
    	public void resetTol(Double tol) {
    		this.tol = tol;
    	}
    	
    	public void resetT() {
    		T.clear();
    	}
    	
    	public Integer sizeT() {
    		return T.size();
    	}
    	
    	public TreeMap<Double, Integer> getT(){
    		return T;
    	}
    	
    	public Integer GreaterThanS(Double s) {
    		SortedMap<Double,Integer> greaterS = T.tailMap(s, false);
    		return greaterS.size();
    	}
    	
    	
    }
    
}
