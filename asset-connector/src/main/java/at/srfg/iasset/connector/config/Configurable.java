package at.srfg.iasset.connector.config;

public interface Configurable<T> {
	
	void configureWith(T config);
	
}
