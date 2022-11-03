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

import at.srfg.iasset.repository.component.ServiceEnvironment;
import at.srfg.iasset.repository.event.EventHandler;
import at.srfg.iasset.repository.event.EventProcessor;
import at.srfg.iasset.repository.utils.ReferenceUtils;

public class EventProcessorImpl implements EventProcessor {
	final ObjectMapper objectMapper;
	final ServiceEnvironment environment;
	// TODO: obtain mapper from environment
	public EventProcessorImpl(ObjectMapper objectMapper, ServiceEnvironment environment) {
		this.objectMapper = objectMapper;
		this.environment = environment;
	}
	public void registerEventElement(BasicEventElement eventElement) {
		// only instance elements may be registered to act!
		if ( eventElement.getKind() == ModelingKind.INSTANCE) {
			
			// check for direction input / output -> create Consumer or Producer
			Direction direction = eventElement.getDirection();
			// topic where to listen / send
			String topic = eventElement.getMessageTopic();
			if ( StringUtils.isEmpty(topic)) {
				return;
			}
			// obtain broker to identify the environment
//			Optional<SubmodelElement> theBroker = resolveMessageBroker(eventElement);
			
			Reference broker = eventElement.getMessageBroker();
			if ( broker != null) {
				Optional<Referable> brokerElement = environment.resolve(broker);
				if ( brokerElement.isPresent()) {
					Referable theBrokerElement = brokerElement.get();
					// check the semanticId of the brokerElement
					String submodelIdentifier = ReferenceUtils.firstKeyValue(broker);
					String path = ReferenceUtils.idShortPath(broker);
					Object brokerData = environment.getElementValue(submodelIdentifier, path);
					
				}
			}
			// observed reference ... required for assigning handlers ...
			Reference observed = eventElement.getObserved();
			// resolve the observed element
			if ( observed != null ) {
				Optional<Referable> observedElement = environment.resolve(observed);
				// 
			}
			// is the processor active or not!
			StateOfEvent state = eventElement.getState();
			// semanticID for the eventElement - different from semanticID of "observed"
			Reference eventSemantic = eventElement.getSemanticId();
			
		}
		
	}
	private Optional<SubmodelElement> resolveMessageBroker(BasicEventElement eventElement) {
		Reference broker = eventElement.getMessageBroker();
		if ( broker == null) {
			// check semantic id - must point to a parent element of kind=Template
			Reference eventSemanticsRef = eventElement.getSemanticId();
			Optional<BasicEventElement> eventSemantics = environment.resolve(eventSemanticsRef, BasicEventElement.class);
			if ( eventSemantics.isPresent()) {
				BasicEventElement parent = eventSemantics.get();
				return resolveMessageBroker(parent);
			}
			return Optional.empty();
		}
		else {
			return environment.resolve(broker, SubmodelElement.class);
		}
		// 
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

}
