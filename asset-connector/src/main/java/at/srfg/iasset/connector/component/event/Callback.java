package at.srfg.iasset.connector.component.event;

public interface Callback<T> {
	void deliveryComplete(T payload);

}
