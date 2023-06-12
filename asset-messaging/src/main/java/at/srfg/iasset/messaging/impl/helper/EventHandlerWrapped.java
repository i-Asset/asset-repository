package at.srfg.iasset.messaging.impl.helper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.eclipse.aas4j.v3.model.EventPayload;
import org.eclipse.aas4j.v3.model.Reference;

import com.fasterxml.jackson.databind.ObjectMapper;

import at.srfg.iasset.messaging.EventHandler;
import at.srfg.iasset.messaging.exception.MessagingException;
import at.srfg.iasset.repository.connectivity.rest.ClientFactory;

public class EventHandlerWrapped<T> {
	private final EventHandler<T> eventHandler;
	private final List<Reference> matching;
	private final ObjectMapper objectMapper = ClientFactory.getObjectMapper();
	
	public EventHandlerWrapped(EventHandler<T> handler, Reference ...matching) {
		this.eventHandler = handler;
		this.matching = Arrays.asList(matching);
	}
	
	public boolean isHandler(EventHandler<?> handler) {
		return eventHandler.equals(handler);
	}

	public boolean matchesReference(List<Reference> references) {
		return matching.stream().allMatch(new Predicate<Reference>() {

			@Override
			public boolean test(Reference t) {
				// each of the provided references must be in the 
				// matching list
				return references.contains(t);
			}});
	}
	public void handleMessage(EventPayload eventPayload, byte[] payload) throws MessagingException {
		T payloadObject = (T) fromMessaging(payload, eventHandler.getPayloadType());
		eventHandler.onEventMessage(eventPayload, payloadObject);
	}

	private T fromMessaging(byte[] incoming, Class<T> clazz) throws MessagingException {
		try {
			return objectMapper.readValue(incoming, clazz);
		} catch (IOException e) {
			throw new MessagingException("Message not readable!");
		}
	}
}
