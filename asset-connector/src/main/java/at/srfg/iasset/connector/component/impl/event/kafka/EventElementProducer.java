package at.srfg.iasset.connector.component.impl.event.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;

import at.srfg.iasset.repository.connectivity.rest.ClientFactory;
import at.srfg.iasset.repository.event.EventProducer;
import at.srfg.iasset.repository.model.helper.EventPayloadHelper;

public class EventElementProducer<T> implements EventProducer<T>{
	private Logger logger = LoggerFactory.getLogger(EventElementProducer.class);
	
	private Sender sender;
	private EventPayloadHelper eventElement;
	
	public EventElementProducer(EventPayloadHelper element) {
		this.eventElement = element;
		
		this.sender = new Sender(eventElement.getTopic());
		//
	}
	
	/**
	 * use the messaging infrastructure to send the (typed) payload
	 */
	@Override
	public void sendEvent(T payload) {
		try {
			sender.sendMessage(eventElement.asPayload(payloadObjectAsString(payload)));
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage());
		}
		
	}
	/**
	 * Helper method to transform the provided (typed) payload into it's string representation
	 * @param payload
	 * @return
	 * @throws JsonProcessingException
	 */
	private String payloadObjectAsString(T payload) throws JsonProcessingException {
		if ( ! String.class.isInstance(payload)) {
			return ClientFactory.getObjectMapper().writeValueAsString(payload);
		}
		else if (Number.class.isInstance(payload) ) {
			return payload.toString();
		}
		return payload.toString();
	}

}
