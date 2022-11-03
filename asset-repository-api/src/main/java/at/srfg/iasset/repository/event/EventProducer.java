package at.srfg.iasset.repository.event;

public interface EventProducer<T> {
	void sendEvent(T payload);
}
