package at.srfg.iasset.connector.environment;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import org.eclipse.aas4j.v3.model.BasicEventElement;
import org.eclipse.aas4j.v3.model.DataElement;
import org.eclipse.aas4j.v3.model.Operation;
import org.eclipse.aas4j.v3.model.Property;
import org.eclipse.aas4j.v3.model.Referable;
import org.eclipse.aas4j.v3.model.Reference;
import org.eclipse.aas4j.v3.model.StateOfEvent;
import org.eclipse.aas4j.v3.model.Submodel;
import org.eclipse.aas4j.v3.model.SubmodelElement;
import org.slf4j.Logger;

import at.srfg.iasset.messaging.ConnectorMessaging;
import at.srfg.iasset.messaging.exception.MessagingException;
import at.srfg.iasset.repository.component.ModelListener;
import at.srfg.iasset.repository.model.helper.visitor.SubmodelElementCollector;
import at.srfg.iasset.repository.utils.SubmodelUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
/**
 * Helper Class dealing with model changes
 * @author dglachs
 *
 */
@ApplicationScoped
public class ChangeProviderCDI implements ChangeProvider {
	@Inject
	private ConnectorMessaging messaging;
	
	@Inject
	private Logger logger;
	
	private final Set<ModelListener> listener = new HashSet<ModelListener>();
	

	private <T extends DataElement> void valueChanged(String submodelIdentifier, String path, T element) {
		// currently only data elements are subject to change events
		listener.forEach(x -> x.dataElementChanged(submodelIdentifier, path, element));
	}
	private <T extends SubmodelElement> void elementCreated(String submodelIdentifier, String pathToElement, T element) {
		listener.forEach(x -> x.submodelElementCreated(submodelIdentifier, pathToElement, element));
	}
	private <T extends SubmodelElement> void elementRemoved(String submodelIdentifier, String path, T element) {
		listener.forEach(x -> x.submodelElementRemoved(submodelIdentifier, path, element));
	}	
	private <T extends Operation> void operationRemoved(String submodelIdentifier, String path, T operation) {
		listener.forEach(t -> t.operationRemoved(submodelIdentifier, path, operation));
	}
	private <T extends Operation> void operationCreated(String submodelIdentifier, String path, T operation) {
		listener.forEach(t -> t.operationCreated(submodelIdentifier, path, operation));
	}
	private <T extends BasicEventElement> void eventElementRemoved(String submodelIdentifier, String path, T eventElement) {
		listener.forEach(t -> t.eventElementRemoved(submodelIdentifier, path, eventElement));
	}
	private <T extends BasicEventElement> void eventElementCreated(String submodelIdentifier, String path, T eventElement) {
		listener.forEach(t -> t.eventElementCreated(submodelIdentifier, path, eventElement));
	}
	private <T extends Property> void propertyRemoved(String submodelIdentifier, String path, T dataElement) {
		listener.forEach(t -> t.propertyRemoved(submodelIdentifier, path, dataElement));
	}
	private <T extends Property> void propertyCreated(String submodelIdentifier, String path, T dataElement) {
		listener.forEach(t -> t.propertyCreated(submodelIdentifier, path, dataElement));
	}

	
	@Override
	public void notifyChange(Submodel submodel, String path, SubmodelElement submodelElement) {
		Map<String, SubmodelElement> elements = new SubmodelElementCollector().collectMap(path, submodelElement);

		elements.forEach(new BiConsumer<String, SubmodelElement>() {

			@Override
			public void accept(String t, SubmodelElement u) {
				if (DataElement.class.isInstance(u)) {
					valueChanged(submodel.getId(), t, DataElement.class.cast(u));
				}

			}
		});
	}

	@Override
	public <T extends Referable> void notifyDeletion(Submodel submodel, String pathToElement, T deletedElement) {
		Map<String, SubmodelElement> elements = new SubmodelElementCollector().collectMap(pathToElement, deletedElement);
		
		elements.forEach(new BiConsumer<String, SubmodelElement>() {

			@Override
			public void accept(String t, SubmodelElement u) {
				if ( BasicEventElement.class.isInstance(u)) {
					eventElementRemoved(t, pathToElement, null);
					// handle removal from messaging
					Reference elementRef = SubmodelUtils.getReference(submodel, pathToElement);
					// TODO: Inject eventProcessor
					messaging.removeEventElement(elementRef);
					
				}
				else if ( Operation.class.isInstance(u)) {
					operationRemoved(submodel.getId(), pathToElement, Operation.class.cast(u));
				}
				else if ( Property.class.isInstance(u)) {
					propertyRemoved(submodel.getId(), pathToElement, Property.class.cast(u));
				} 
				else {
					elementRemoved(submodel.getId(), pathToElement, u);
				}
				
			}
		});

	}

	@Override
	public <T extends Referable> void notifyCreation(Submodel submodel, String pathToElement, T createdElement) {
		new SubmodelElementCollector().collectMap(pathToElement, createdElement).forEach(new BiConsumer<String, SubmodelElement>() {

			@Override
			public void accept(String pathToElement, SubmodelElement u) {
				if ( BasicEventElement.class.isInstance(u)) {
					BasicEventElement basicEvent = (BasicEventElement)u;
					Reference elementRef = SubmodelUtils.getReference(submodel, pathToElement);
					try {
						messaging.registerEventElement(elementRef);
						eventElementCreated(submodel.getId(), pathToElement, BasicEventElement.class.cast(u));
					} catch (MessagingException e) {
						// deactivate the event
						logger.warn(e.getLocalizedMessage());
						basicEvent.setState(StateOfEvent.OFF);
						
					}
					
				}
				else if ( Operation.class.isInstance(u)) {
					operationCreated(submodel.getId(), pathToElement, Operation.class.cast(u));
				}
				else if ( Property.class.isInstance(u)) {
					propertyCreated(submodel.getId(), pathToElement, Property.class.cast(u));
				}
				else {
					elementCreated(submodel.getId(), pathToElement, u);
				}
					
				
			}
		});
	}

	@Override
	public void addModelListener(ModelListener listener) {
		this.listener.add(listener);
		
	}

	@Override
	public void removeModelListener(ModelListener listener) {
		this.listener.remove(listener);
		
	}

}
