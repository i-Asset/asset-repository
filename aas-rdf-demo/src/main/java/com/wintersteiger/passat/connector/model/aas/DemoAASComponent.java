package com.wintersteiger.passat.connector.model.aas;

import org.eclipse.digitaltwin.aas4j.v3.model.BasicEventElement;
import org.eclipse.digitaltwin.aas4j.v3.model.DataElement;
import org.eclipse.digitaltwin.aas4j.v3.model.Operation;
import org.eclipse.digitaltwin.aas4j.v3.model.Property;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElement;
import org.slf4j.Logger;

import at.srfg.iasset.connector.component.AASComponentModel;
import at.srfg.iasset.connector.environment.LocalEnvironment;
import at.srfg.iasset.repository.component.ModelListener;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

@ApplicationScoped
public class WintersteigerAAS implements AASComponentModel {
	@Inject
	private Logger logger;

	@Inject
	@Any
	Instance<AASModelData> modelData;
	@Inject
	@Any
	Instance<AASModelLogic> modelLogic;

	@Override
	public void loadData(LocalEnvironment environment) {
		environment.addModelListener(new ModelListener() {
			
			@Override
			public void submodelElementRemoved(String submodelIdentifier, String path, SubmodelElement element) {
				logger.info("SubodelElement gelöscht: {} / {}", submodelIdentifier, path);
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void submodelElementCreated(String submodelIdentifier, String path, SubmodelElement element) {
				logger.info("SubodelElement erzeugt: {} / {}", submodelIdentifier, path);
				
			}
			
			@Override
			public void propertyRemoved(String submodelIdentifier, String path, Property property) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void propertyCreated(String submodelIdentifier, String path, Property property) {
				logger.info("Property erzeugt: {} / {}", submodelIdentifier, path);
				
			}
			
			@Override
			public void operationRemoved(String submodelIdentifier, String path, Operation operation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void operationCreated(String submodelIdentifier, String path, Operation operation) {
				logger.info("Operation erzeugt: {} / {}", submodelIdentifier, path);
				
			}
			
			@Override
			public void eventElementRemoved(String submodelIdentifier, String path, BasicEventElement eventElement) {
				
			}
			
			@Override
			public void eventElementCreated(String submodelIdentifier, String path, BasicEventElement eventElement) {
				logger.info("Event Element erzeugt: {} / {}", submodelIdentifier, path);
			}
			
			@Override
			public void dataElementChanged(String submodelIdentifier, String path, DataElement property) {
				// TODO Auto-generated method stub
				
			}
		});
		for ( AASModelData part : modelData) {
			part.loadData(environment);
		}
	}

	@Override
	public void injectLogic(LocalEnvironment environment) {
		for ( AASModelLogic part : modelLogic) {
			part.injectLogic(environment);
		}
	}
	
	
	
}
