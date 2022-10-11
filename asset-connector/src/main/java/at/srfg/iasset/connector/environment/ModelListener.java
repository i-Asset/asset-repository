package at.srfg.iasset.connector.environment;

import org.eclipse.aas4j.v3.model.EventElement;
import org.eclipse.aas4j.v3.model.Operation;
import org.eclipse.aas4j.v3.model.Property;
import org.eclipse.aas4j.v3.model.SubmodelElement;

public interface ModelListener {
	
	void submodelElementCreated(String path, SubmodelElement element);
	void propertyCreated(String path, Property property);
	void propertyRemoved(String path);
	void propertyValueChanged(String path, String oldValue, String newValue);
	void operationCreated(String path, Operation operation);
	void operationRemoved(String path);
	void eventElementCreated(String path, EventElement eventElement);
	void eventElementRemoved(String path);
	

}
