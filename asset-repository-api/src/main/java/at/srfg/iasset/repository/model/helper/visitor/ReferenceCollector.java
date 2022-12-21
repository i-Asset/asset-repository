package at.srfg.iasset.repository.model.helper.visitor;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import org.eclipse.aas4j.v3.dataformat.core.visitor.AssetAdministrationShellElementWalkerVisitor;
import org.eclipse.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.aas4j.v3.model.BasicEventElement;
import org.eclipse.aas4j.v3.model.Key;
import org.eclipse.aas4j.v3.model.KeyTypes;
import org.eclipse.aas4j.v3.model.Operation;
import org.eclipse.aas4j.v3.model.Property;
import org.eclipse.aas4j.v3.model.Referable;
import org.eclipse.aas4j.v3.model.Reference;
import org.eclipse.aas4j.v3.model.ReferenceTypes;
import org.eclipse.aas4j.v3.model.Submodel;
import org.eclipse.aas4j.v3.model.SubmodelElementCollection;
import org.eclipse.aas4j.v3.model.impl.DefaultKey;
import org.eclipse.aas4j.v3.model.impl.DefaultReference;

import at.srfg.iasset.repository.component.ServiceEnvironment;
import at.srfg.iasset.repository.utils.ReferenceUtils;

public class ReferenceCollector {
	private final ServiceEnvironment environment;
	private final KeyTypes typeToCheck;
	public ReferenceCollector(ServiceEnvironment environment, KeyTypes typeToCheck) {
		this.environment = environment;
		this.typeToCheck = typeToCheck;
		
	}
	
    public <T extends Referable> Set<Reference> collect(T root) {
        Visitor visitor = new Visitor();
        visitor.visit(root);
        return visitor.element;
    }

    private class Visitor implements AssetAdministrationShellElementWalkerVisitor {

    	Reference modelReference; 
    	Visitor() {
    		
    		modelReference = new DefaultReference.Builder()
    				.type(ReferenceTypes.MODEL_REFERENCE)
    				// keys are collected 
    				.build();
    		
    	}
    	
    	Set<Reference> element = new HashSet<>();
    	
		@Override
		public void visit(AssetAdministrationShell assetAdministrationShell) {
			Key elementKey = new DefaultKey.Builder()
					.type(ReferenceUtils.referableToKeyType(assetAdministrationShell))
					.value(assetAdministrationShell.getId())
					.build();
			modelReference.getKeys().add(elementKey);
			// 
			assetAdministrationShell.getSubmodels().forEach(new Consumer<Reference>() {

				@Override
				public void accept(Reference t) {
					environment.resolve(t, Submodel.class).ifPresent(new Consumer<Submodel>() {

						@Override
						public void accept(Submodel t) {
							Reference cloned = ReferenceUtils.clone(modelReference);
							visit(t);
							modelReference = cloned;
							
						}});
					
					
				}
			});
			AssetAdministrationShellElementWalkerVisitor.super.visit(assetAdministrationShell);
		}

		@Override
		public void visit(Submodel submodel) {
			// TODO Auto-generated method stub
			//
			Key elementKey = new DefaultKey.Builder()
					.type(ReferenceUtils.referableToKeyType(submodel))
					.value(submodel.getId())
					.build();
			modelReference.getKeys().add(elementKey);

			Reference cloned = ReferenceUtils.clone(modelReference);
			if ( typeToCheck == null || KeyTypes.SUBMODEL.equals(typeToCheck)) {
				element.add(modelReference);
			}
			else {
				AssetAdministrationShellElementWalkerVisitor.super.visit(submodel);
			}
			modelReference = cloned;
		}

		@Override
		public void visit(Property property) {
			if ( typeToCheck == null || KeyTypes.PROPERTY.equals(typeToCheck)) {
				Reference clonedInner = ReferenceUtils.clone(modelReference);
				Key elementKey = new DefaultKey.Builder()
						.type(ReferenceUtils.referableToKeyType(property))
						.value(property.getIdShort())
						.build();
				modelReference.getKeys().add(elementKey);
				element.add(modelReference);
				AssetAdministrationShellElementWalkerVisitor.super.visit(property);
				modelReference = clonedInner;
			}
		}

		@Override
		public void visit(SubmodelElementCollection submodelElementCollection) {
			//

			Key elementKey = new DefaultKey.Builder()
					.type(ReferenceUtils.referableToKeyType(submodelElementCollection))
					.value(submodelElementCollection.getIdShort())
					.build();
			modelReference.getKeys().add(elementKey);

			Reference clonedInner = ReferenceUtils.clone(modelReference);
			if ( KeyTypes.SUBMODEL_ELEMENT_COLLECTION.equals(typeToCheck)) {
				element.add(modelReference);
			}
			else {
				AssetAdministrationShellElementWalkerVisitor.super.visit(submodelElementCollection);
			}
			modelReference = clonedInner;
		}

		@Override
		public void visit(Operation operation) {
			if ( typeToCheck == null || KeyTypes.OPERATION.equals(typeToCheck)) {
				Reference clonedInner = ReferenceUtils.clone(modelReference);
				Key elementKey = new DefaultKey.Builder()
						.type(ReferenceUtils.referableToKeyType(operation))
						.value(operation.getIdShort())
						.build();
				modelReference.getKeys().add(elementKey);
				element.add(modelReference);
				modelReference = clonedInner;
			}			
		}

		@Override
		public void visit(BasicEventElement basicEvent) {
			if ( typeToCheck == null || KeyTypes.BASIC_EVENT_ELEMENT.equals(typeToCheck)) {
				Reference clonedInner = ReferenceUtils.clone(modelReference);
				
				Key elementKey = new DefaultKey.Builder()
						.type(ReferenceUtils.referableToKeyType(basicEvent))
						.value(basicEvent.getIdShort())
						.build();
				modelReference.getKeys().add(elementKey);
				element.add(modelReference);
				modelReference = clonedInner;
			}
		}
        
    }

}
