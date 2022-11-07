package at.srfg.iasset.repository.component;

import org.eclipse.aas4j.v3.model.BasicEventElement;
import org.eclipse.aas4j.v3.model.DataElement;
import org.eclipse.aas4j.v3.model.Operation;
import org.eclipse.aas4j.v3.model.Property;
import org.eclipse.aas4j.v3.model.SubmodelElement;

public interface ModelListener {
	
	void submodelElementCreated(String path, SubmodelElement element);
	void propertyCreated(String path, Property property);
	void dataElementChanged(String path, DataElement property);
	void propertyRemoved(String path, Property property);
	void operationCreated(String path, Operation operation);
	void operationRemoved(String path, Operation operation);
	void eventElementCreated(String path, BasicEventElement eventElement);
	void eventElementRemoved(String path, BasicEventElement eventElement);
	

}
