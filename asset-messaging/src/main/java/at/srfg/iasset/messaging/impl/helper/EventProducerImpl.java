package at.srfg.iasset.messaging.impl.helper;

import org.eclipse.digitaltwin.aas4j.v3.model.Reference;

import at.srfg.iasset.messaging.EventElementHandler;
import at.srfg.iasset.messaging.EventProducer;
import at.srfg.iasset.messaging.exception.MessagingException;
import at.srfg.iasset.repository.utils.ReferenceUtils;

public class EventProducerImpl<T> implements EventProducer<T> {
	private final EventElementHandler handler;
	public EventProducerImpl(Class<T> clazz, EventElementHandler handler) {
		this.handler = handler;
	}

	@Override
	public void sendEvent(T payload) throws MessagingException {
		handler.sendEvent(payload);
		
	}

	@Override
	public void sendEvent(T payload, Reference subjectId) throws MessagingException {
		handler.sendEvent(payload, subjectId);
	}
	@Override
	public void close() {
		if (handler.isActive()) {
			handler.shutdown();
		}
	}

	@Override
	public <U> void sendEvent(T payload, String subjectId) throws MessagingException {
		sendEvent(payload, ReferenceUtils.asGlobalReference(subjectId));
		
	}
}
