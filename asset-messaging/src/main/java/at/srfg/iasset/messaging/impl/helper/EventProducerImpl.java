package at.srfg.iasset.messaging.impl.helper;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.eclipse.aas4j.v3.model.Reference;

import com.fasterxml.jackson.databind.ObjectMapper;

import at.srfg.iasset.messaging.EventElementHandler;
import at.srfg.iasset.messaging.EventHandler;
import at.srfg.iasset.messaging.EventProducer;
import at.srfg.iasset.messaging.exception.MessagingException;
import at.srfg.iasset.repository.connectivity.rest.ClientFactory;

public class EventProducerImpl<T> implements EventProducer<T> {
	private final Class<T> type;
	private final EventElementHandler handler;
	private Map<Reference, EventHandler<?>> dependentHandler = new HashMap<>();
	public EventProducerImpl(Class<T> clazz, EventElementHandler handler) {
		this.type = clazz;
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
	private <H> void registerHandler(Reference subjectId, EventHandler<H> eventHandler) throws MessagingException {
		handler.registerHandler(eventHandler, subjectId);
		dependentHandler.put(subjectId, eventHandler);
	}

	@Override
	public void close() {
		dependentHandler.values().forEach(new Consumer<EventHandler<?>>() {

			@Override
			public void accept(EventHandler<?> t) {
				try {
					handler.removeHandler(t);
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
	}

	@Override
	public <U> void sendEvent(T payload, Reference subjectId, EventHandler<U> updateHandler) throws MessagingException {
		sendEvent(payload, subjectId);
		registerHandler(subjectId, updateHandler);
		
	}
}
