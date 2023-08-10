package at.srfg.iasset.connector;

import org.eclipse.aas4j.v3.model.BasicEventElement;
import org.eclipse.aas4j.v3.model.DataElement;
import org.eclipse.aas4j.v3.model.Operation;
import org.eclipse.aas4j.v3.model.Property;
import org.eclipse.aas4j.v3.model.SubmodelElement;

import at.srfg.iasset.repository.component.ModelListener;


public class ModelChangeLogger implements ModelListener {
	@Override
	public void submodelElementCreated(String submodelIdentifier, String path, SubmodelElement element) {
		System.out.println(submodelIdentifier + "/"+ path + " created");
		
	}
	
	@Override
	public void submodelElementRemoved(String submodelIdentifier, String path, SubmodelElement element) {
		System.out.println(submodelIdentifier + "/"+ path + " removed");
		
	}
	
	@Override
	public void propertyCreated(String submodelIdentifier, String path, Property property) {
		System.out.println(submodelIdentifier + "/"+ path + " created");
		
	}
	
	@Override
	public void dataElementChanged(String submodelIdentifier, String path, DataElement property) {
		System.out.println(submodelIdentifier + "/"+ path + " value changed");
		
	}
	
	@Override
	public void propertyRemoved(String submodelIdentifier, String path, Property property) {
		System.out.println(submodelIdentifier + "/"+ path + " removed");
		
	}
	
	@Override
	public void operationCreated(String submodelIdentifier, String path, Operation operation) {
		System.out.println(submodelIdentifier + "/"+ path + " created");
		
	}
	
	@Override
	public void operationRemoved(String submodelIdentifier, String path, Operation operation) {
		System.out.println(submodelIdentifier + "/"+ path + " removed");
		
	}
	
	@Override
	public void eventElementCreated(String submodelIdentifier, String path, BasicEventElement eventElement) {
		System.out.println(submodelIdentifier + "/"+ path + " created");
		
	}
	
	@Override
	public void eventElementRemoved(String submodelIdentifier, String path, BasicEventElement eventElement) {
		System.out.println(submodelIdentifier + "/"+ path + " removed");

		
	}
}
