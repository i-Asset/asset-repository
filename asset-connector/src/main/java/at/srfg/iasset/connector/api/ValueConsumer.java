package at.srfg.iasset.connector.api;

public interface ValueConsumer<T> {
	/**
	 * Accept a new typed value 
	 * @param value
	 */
	public void accept(T value);

}
