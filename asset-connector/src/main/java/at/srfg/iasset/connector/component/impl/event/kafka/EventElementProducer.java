package at.srfg.iasset.connector.component.impl.event.kafka;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;

import at.srfg.iasset.connector.component.event.Callback;
import at.srfg.iasset.connector.component.event.EventProducer;
import at.srfg.iasset.connector.component.impl.event.EventPayloadHelper;
import at.srfg.iasset.repository.connectivity.rest.ClientFactory;

public class EventElementProducer<T> implements EventProducer<T>{
	private Logger logger = LoggerFactory.getLogger(EventElementProducer.class);
	
	private Sender sender;
	private EventPayloadHelper eventElement;
	
	public EventElementProducer(EventPayloadHelper element) {
		this.eventElement = element;
		
		this.sender = new Sender(eventElement.getTopic());
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				sender.close();
			}
		});

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
	 * use the messaging infrastructure to send the (typed) payload
	 */
	@Override
	public void sendEvent(T payload, Callback<T> callBack) {
		try {
			sender.sendMessage(eventElement.asPayload(payloadObjectAsString(payload)), new org.apache.kafka.clients.producer.Callback() {
				
				@Override
				public void onCompletion(RecordMetadata metadata, Exception exception) {
					if  (exception == null) {
						// notify callback
						callBack.deliveryComplete(payload);
					}
					else {
						// 
					}
				}
			});
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
	public void stop() {
		this.sender.close();
	}

	@Override
	public void close() {
		if ( this.sender != null) {
			this.sender.close();
		}
		
	}

}
