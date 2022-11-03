package at.srfg.iasset.connector.component.impl.event;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.aas4j.v3.model.BasicEventElement;
import org.eclipse.aas4j.v3.model.Direction;
import org.eclipse.aas4j.v3.model.EventPayload;
import org.eclipse.aas4j.v3.model.KeyTypes;
import org.eclipse.aas4j.v3.model.ModelingKind;
import org.eclipse.aas4j.v3.model.Referable;
import org.eclipse.aas4j.v3.model.Reference;
import org.eclipse.aas4j.v3.model.StateOfEvent;
import org.eclipse.aas4j.v3.model.SubmodelElement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import at.srfg.iasset.connector.component.impl.event.kafka.EventElementConsumer;
import at.srfg.iasset.connector.component.impl.event.kafka.EventElementProducer;
import at.srfg.iasset.repository.component.ServiceEnvironment;
import at.srfg.iasset.repository.event.EventHandler;
import at.srfg.iasset.repository.event.EventProcessor;
import at.srfg.iasset.repository.utils.ReferenceUtils;

public class EventProcessorImpl implements EventProcessor {
	final ObjectMapper objectMapper;
	final ServiceEnvironment environment;
	final Map<BasicEventElement, EventElementConsumer> eventConsumer;
	final Map<BasicEventElement, EventElementProducer> eventProducer;
	// 
	public EventProcessorImpl(ObjectMapper objectMapper, ServiceEnvironment environment) {
		this.objectMapper = objectMapper;
		this.environment = environment;
		this.eventConsumer = new HashMap<BasicEventElement, EventElementConsumer>();
		this.eventProducer = new HashMap<BasicEventElement, EventElementProducer>();
	}
	public void registerEventElement(BasicEventElement eventElement) {
		
		// only instance elements may be registered to act!
		if ( ModelingKind.INSTANCE.equals(eventElement.getKind()) ) {
			if ( eventElement.getDirection()!= null) {
				switch (eventElement.getDirection()) {
				case INPUT:
					//
					EventElementConsumer consumer = new EventElementConsumer(this, eventElement);
					eventConsumer.put(eventElement, consumer);
					break;
				case OUTPUT:
					EventElementProducer producer = new EventElementProducer(eventElement);
					eventProducer.put(eventElement, producer);
				}
			}
		}	
	}

	/**
	 * Mapping for EventListeners
	 */
	final Map<String, Map<Reference, EventHandler<?>>> map = new HashMap<String, Map<Reference,EventHandler<?>>>();
	
	
	@Override
	public void processIncomingMessage(String topic, String key, String message) {
		// 
		try {
			EventPayload fullPayload = objectMapper.readerFor(EventPayload.class).readValue(message);
			
			findHandler(topic, fullPayload.getObservableSemanticId()).ifPresent(new Consumer<EventHandler<?>>() {

				@Override
				public void accept(EventHandler<?> t) {
					try {
						Object payload = objectMapper.readerFor(t.getPayloadType()).readValue(fullPayload.getPayload());
						
					} catch (JsonProcessingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			});;
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Override
	public <T> void registerHandler(String aasIdentifier, String submodelIdentifier, String semanticId,
			EventHandler<T> handler) {
		registerHandler(aasIdentifier, submodelIdentifier, ReferenceUtils.asGlobalReference(KeyTypes.GLOBAL_REFERENCE, semanticId), handler);
	}
	@Override
	public <T> void registerHandler(String aasIdentifier, String submodelIdentifier, Reference semanticId,
			EventHandler<T> handler) {
		environment.getSubmodelElements(aasIdentifier, submodelIdentifier, semanticId, BasicEventElement.class)
				.stream().filter(new Predicate<BasicEventElement>() {

					@Override
					public boolean test(BasicEventElement t) {
						// only INPUT events
						return Direction.INPUT == t.getDirection();
					}
				})
				.forEach(new Consumer<BasicEventElement>() {

					@Override
					public void accept(BasicEventElement t) {
						if (! map.containsKey(t.getMessageTopic())) {
							map.put(t.getMessageTopic(), new HashMap<Reference, EventHandler<?>>());
						}
						Map<Reference, EventHandler<?>> refMap = map.get(t.getMessageTopic());
						refMap.put(semanticId, handler);
					}
				});
		
		
	}


	@Override
	public void sendTestEvent(String string, String semanticId, Object payload) {
		Optional<EventHandler<?>> handler = findHandler(string, ReferenceUtils.asGlobalReference(KeyTypes.GLOBAL_REFERENCE, semanticId));
		handler.ifPresent(new Consumer<EventHandler<?>>() {

			@Override
			public void accept(EventHandler<?> t) {
				acceptPayload(t, payload);
				
			}
			private <T> void acceptPayload(EventHandler<T> handler, Object payload) {
				T val = objectMapper.convertValue(payload, handler.getPayloadType());
				handler.onEventMessage(null, val);
			}
		});
		
	}
	private Optional<EventHandler<?>> findHandler(String topic, Reference semanticRef) {
		if ( map.containsKey(topic)) {
			return map.get(topic).entrySet().stream()
				.filter(new Predicate<Entry<Reference, EventHandler<?>>>() {

					@Override
					public boolean test(Entry<Reference, EventHandler<?>> t) {
						return semanticRef.equals(t.getKey());
					}
				})
				.map(new Function<Entry<Reference, EventHandler<?>>, EventHandler<?>>() {

					@Override
					public EventHandler<?> apply(Entry<Reference, EventHandler<?>> t) {
						return t.getValue();
					}
				})
				.findAny();
		}
		return Optional.empty();
		
	}
	@Override
	public void startEventProcessing() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void stopEventProcessing() {
		// TODO Auto-generated method stub
		
	}

}
