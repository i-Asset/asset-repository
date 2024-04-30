package at.srfg.iasset.repository.model;

import java.util.Date;

public class Sensor implements Featureable{
	public String assetId;
	public String sensorId;
	public String subElementPath;
	public String measurementId;
	public String timestampCreated;
	public String measurement;
	
	
	public String getAssetId() {
		return assetId;
	}
	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}
	public String getSubElementPath() {
		return subElementPath;
	}
	public void setSubElementPath(String subElementPath) {
		this.subElementPath = subElementPath;
	}
	@Override
	public String getTimestampCreated() {
		return timestampCreated;
	}
	public void setTimestampCreated(String timestampCreated) {
		this.timestampCreated = timestampCreated;
	}
	@Override
	public String[] getFeatureableValue() {
		return new String[] {measurement};
	}
	public void setMeasurement(String measurement) {
		this.measurement = measurement;
	}
	
	public void setSensorID(String sensorId) {
		this.sensorId = sensorId;
	}

}
