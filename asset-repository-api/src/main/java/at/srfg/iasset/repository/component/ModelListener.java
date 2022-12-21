package at.srfg.iasset.repository.component;

import org.eclipse.aas4j.v3.model.BasicEventElement;
import org.eclipse.aas4j.v3.model.DataElement;
import org.eclipse.aas4j.v3.model.Operation;
import org.eclipse.aas4j.v3.model.Property;
import org.eclipse.aas4j.v3.model.Submodel;
import org.eclipse.aas4j.v3.model.SubmodelElement;
/**
 * Interface propagating model changes to the asset or application. 
 * @author dglachs
 *
 */
public interface ModelListener {
	/**
	 * Method called whenever a new {@link SubmodelElement} has been added to
	 * the environment
	 * @param submodelIdentifier The <code>id</code> of the {@link Submodel}, the element belongs to 
	 * @param path The <code>idShort</code>-path pointing to the {@link SubmodelElement}
	 * @param element The newly added {@link SubmodelElement}
	 */
	void submodelElementCreated(String submodelIdentifier, String path, SubmodelElement element);
	/**
	 * Method called whenever a new {@link SubmodelElement} has been removed from
	 * the environment
	 * @param submodelIdentifier The <code>id</code> of the {@link Submodel}, the element belongs to 
	 * @param path The <code>idShort</code>-path pointing to the {@link SubmodelElement}
	 * @param element The newly added {@link SubmodelElement}
	 */
	void submodelElementRemoved(String submodelIdentifier, String path, SubmodelElement element);
	/**
	 * Method called whenever a new {@link Property} has been added to
	 * the environment
	 * @param submodelIdentifier The <code>id</code> of the {@link Submodel}, the element belongs to 
	 * @param path The <code>idShort</code>-path pointing to the {@link Property}
	 * @param property The newly added {@link Property}
	 */
	void propertyCreated(String submodelIdentifier, String path, Property property);
	
	void dataElementChanged(String submodelIdentifier, String path, DataElement property);
	/**
	 * Method called whenever a new {@link Property} has been removed from
	 * the environment
	 * @param submodelIdentifier The <code>id</code> of the {@link Submodel}, the element belongs to 
	 * @param path The <code>idShort</code>-path pointing to the {@link Property}
	 * @param property The newly added {@link Property}
	 */
	void propertyRemoved(String submodelIdentifier, String path, Property property);
	/**
	 * Method called whenever a new {@link Operation} has been added to
	 * the environment
	 * @param submodelIdentifier The <code>id</code> of the {@link Submodel}, the element belongs to 
	 * @param path The <code>idShort</code>-path pointing to the {@link Operation}
	 * @param operation The newly added {@link Operation}
	 */
	void operationCreated(String submodelIdentifier, String path, Operation operation);
	/**
	 * Method called whenever a new {@link Operation} has been removed from
	 * the environment
	 * @param submodelIdentifier The <code>id</code> of the {@link Submodel}, the element belongs to 
	 * @param path The <code>idShort</code>-path pointing to the {@link Operation}
	 * @param operation The newly added {@link Operation}
	 */
	void operationRemoved(String submodelIdentifier, String path, Operation operation);
	/**
	 * Method called whenever a new {@link BasicEventElement} has been added to
	 * the environment
	 * @param submodelIdentifier The <code>id</code> of the {@link Submodel}, the element belongs to 
	 * @param path The <code>idShort</code>-path pointing to the {@link BasicEventElement}
	 * @param eventElement The newly added {@link BasicEventElement}
	 */
	void eventElementCreated(String submodelIdentifier, String path, BasicEventElement eventElement);
	/**
	 * Method called whenever a new {@link BasicEventElement} has been added to
	 * the environment
	 * @param submodelIdentifier The <code>id</code> of the {@link Submodel}, the element belongs to 
	 * @param path The <code>idShort</code>-path pointing to the {@link BasicEventElement}
	 * @param eventElement The newly added {@link BasicEventElement}
	 */
	void eventElementRemoved(String submodelIdentifier, String path, BasicEventElement eventElement);
	

}
