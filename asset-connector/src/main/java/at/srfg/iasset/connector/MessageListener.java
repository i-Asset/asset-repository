package at.srfg.iasset.connector;

import org.eclipse.aas4j.v3.model.Reference;

public interface MessageListener<T> {
	public void onMessage(Reference semanticId, T payload);
}
