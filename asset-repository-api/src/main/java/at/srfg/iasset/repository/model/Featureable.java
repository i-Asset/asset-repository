package at.srfg.iasset.repository.model;

public interface Featureable {

	/**
	 * Return Timestamp of feature creation
	 * Necessary for time-series operations
	 */
	public String getTimestampCreated();
	
	/**
	 * Return Measurement value from which Features should be computed
	 */
	public String[] getFeatureableValue();
	
}
