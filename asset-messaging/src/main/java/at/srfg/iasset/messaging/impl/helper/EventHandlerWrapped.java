package at.srfg.iasset.messaging.impl.helper;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.eclipse.digitaltwin.aas4j.v3.model.EventPayload;
import org.eclipse.digitaltwin.aas4j.v3.model.Referable;
import org.eclipse.digitaltwin.aas4j.v3.model.Reference;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElement;
import org.jboss.weld.exceptions.IllegalStateException;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import at.srfg.iasset.messaging.EventHandler;
import at.srfg.iasset.messaging.exception.MessagingException;
import at.srfg.iasset.repository.model.helper.ValueHelper;
import at.srfg.iasset.repository.model.helper.value.SubmodelElementValue;
import at.srfg.iasset.repository.model.helper.value.exception.ValueMappingException;
/**
 * Helper class managing the transformation of the
 * individual payload objects 
 * @author dglachs
 *
 * @param <T>
 */
public class EventHandlerWrapped<T> {
	private final EventHandler<T> eventHandler;
	private final List<Reference> matching;
	private final SubmodelElement observed;
	// TODO: provide injected object mapper
	private final ObjectMapper objectMapper;
	private final JavaType javaType;

	public EventHandlerWrapped(ObjectMapper objectMapper, EventHandler<T> handler, Referable observed, Reference ... matching) {
		this.eventHandler = handler;
		// 
		this.observed = (SubmodelElement.class.isInstance(observed) ? SubmodelElement.class.cast(observed) : null);
		this.matching = Arrays.asList(matching);
		this.objectMapper = objectMapper;
		
		// construct the generic JavaType
		javaType = objectMapper.getTypeFactory().constructType(genericType(handler));
	}
	private Type genericType(EventHandler<?> handler) {
		Type[] types = handler.getClass().getGenericInterfaces();
		if ( types.length > 0) {
			ParameterizedType pType = (ParameterizedType) types[0];
			Type[] arguments = pType.getActualTypeArguments();
			if ( arguments.length > 0 ) {
				return arguments[0];
			}
		}
		throw new IllegalStateException("Cannot extract the generic type from EventHandler");
	}
	public boolean isHandler(EventHandler<?> handler) {
		return eventHandler.equals(handler);
	}

	public boolean matchesReference(List<Reference> references) {
		return matching.stream().anyMatch(new Predicate<Reference>() {

			@Override
			public boolean test(Reference t) {
				// each of the provided references must be in the 
				// matching list
				return references.contains(t);
			}});
	}
	public void handleMessage(EventPayload eventPayload, byte[] payload) throws MessagingException {
		// we use the etracted java type
		T payloadObject = (T) fromMessaging(payload, javaType);
		eventHandler.onEventMessage(eventPayload, payloadObject);
	}

	private T fromMessaging(byte[] incoming, JavaType clazz) throws MessagingException {
		try {
			// can we perform validation
			if ( observed !=null ) {
				// unpack with validation
				Object payloadValue = toValue(incoming);
				// now transform the value to the requested type!
				return objectMapper.convertValue(payloadValue, clazz);
			}
			else {
				// Observed is not a submodel element
				return objectMapper.readValue(incoming, clazz);
			}
		} catch (IOException e) {
			throw new MessagingException("Message not readable!");
		} catch (ValueMappingException e) {
			throw new MessagingException(e);
		}
	}
	/**
	 * Map the incoming byte stream to the observed element. 
	 * @param incoming
	 * @return The {@link SubmodelElementValue} of the observed element.
	 * @throws IOException
	 * @throws ValueMappingException 
	 */
	private SubmodelElementValue toValue(byte[] incoming) throws IOException, ValueMappingException {
		JsonNode valueAsNode = objectMapper.readTree(incoming);
		// apply value conversion 
		ValueHelper.applyValue(observed, valueAsNode);
		return ValueHelper.toValue(observed);
	}
}
