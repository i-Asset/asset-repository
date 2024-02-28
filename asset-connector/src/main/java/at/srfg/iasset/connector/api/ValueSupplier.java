package at.srfg.iasset.connector.api;

public interface ValueSupplier<T> {
	/**
	 * Provide the typed value 
	 * @return
	 */
	T get();
}
