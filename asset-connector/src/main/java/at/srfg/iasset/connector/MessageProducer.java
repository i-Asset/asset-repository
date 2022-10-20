package at.srfg.iasset.connector;

import org.eclipse.aas4j.v3.model.Reference;

public interface MessageProducer<T> {
	public void send(Reference semanticId, T payload);

}
