package at.srfg.iasset.connector.environment;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.eclipse.aas4j.v3.dataformat.core.util.AasUtils;
import org.eclipse.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.aas4j.v3.model.ConceptDescription;
import org.eclipse.aas4j.v3.model.EventElement;
import org.eclipse.aas4j.v3.model.KeyTypes;
import org.eclipse.aas4j.v3.model.Operation;
import org.eclipse.aas4j.v3.model.Property;
import org.eclipse.aas4j.v3.model.Referable;
import org.eclipse.aas4j.v3.model.Reference;
import org.eclipse.aas4j.v3.model.RelationshipElement;
import org.eclipse.aas4j.v3.model.Submodel;
import org.eclipse.aas4j.v3.model.SubmodelElement;

import at.srfg.iasset.connector.component.impl.AASFull;
import at.srfg.iasset.repository.component.ServiceEnvironment;
import at.srfg.iasset.repository.model.custom.InstanceEnvironment;
import at.srfg.iasset.repository.model.custom.InstanceOperation;
import at.srfg.iasset.repository.model.custom.InstanceProperty;
import at.srfg.iasset.repository.model.helper.SubmodelHelper;
import at.srfg.iasset.repository.utils.ReferenceUtils;

public class LocalServiceEnvironment implements ServiceEnvironment, LocalEnvironment {
	private InstanceEnvironment environment;
	
	private Collection<ModelListener> listeners = new HashSet<ModelListener>();
	
	
	public LocalServiceEnvironment() {
		environment = new InstanceEnvironment();
		// TODO: REMOVE test data!
		environment.addAssetAdministrationShell(AASFull.AAS_1.getId(), AASFull.AAS_1);
		environment.addAssetAdministrationShell(AASFull.AAS_2.getId(), AASFull.AAS_2);
		environment.addAssetAdministrationShell(AASFull.AAS_3.getId(), AASFull.AAS_3);
		environment.addAssetAdministrationShell(AASFull.AAS_4.getId(), AASFull.AAS_4);
		environment.addSubmodel(AASFull.SUBMODEL_1.getId(), AASFull.SUBMODEL_1);
		environment.addSubmodel(AASFull.SUBMODEL_2.getId(), AASFull.SUBMODEL_2);
		environment.addSubmodel(AASFull.SUBMODEL_3.getId(), AASFull.SUBMODEL_3);
		environment.addSubmodel(AASFull.SUBMODEL_4.getId(), AASFull.SUBMODEL_4);
		environment.addSubmodel(AASFull.SUBMODEL_5.getId(), AASFull.SUBMODEL_5);
		environment.addSubmodel(AASFull.SUBMODEL_6.getId(), AASFull.SUBMODEL_6);
		environment.addSubmodel(AASFull.SUBMODEL_7.getId(), AASFull.SUBMODEL_7);
		environment.addConceptDescription(AASFull.CONCEPT_DESCRIPTION_1);
		environment.addConceptDescription(AASFull.CONCEPT_DESCRIPTION_2);
		environment.addConceptDescription(AASFull.CONCEPT_DESCRIPTION_3);
		environment.addConceptDescription(AASFull.CONCEPT_DESCRIPTION_4);
		listeners.add(new ModelListener() {
			
			@Override
			public void propertyValueChanged(String path, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void propertyRemoved(String path) {
				System.out.println("Element removed: "+ path);
				
			}
			
			@Override
			public void propertyCreated(String path, org.eclipse.aas4j.v3.model.Property property) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void operationRemoved(String path) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void operationCreated(String path, Operation operation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void eventElementRemoved(String path) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void eventElementCreated(String path, EventElement eventElement) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void submodelElementCreated(String path, SubmodelElement element) {
				System.out.println("Element created: " + path + " - " + element.getIdShort());
				
			}
		});
	}
	
	public void addModelListener(ModelListener listener) {
		listeners.add(listener);
	}
	public void removeModelListener(ModelListener listener) {
		listeners.remove(listener);
	}

	@Override
	public Optional<Submodel> getSubmodel(String aasIdentifier, String submodelIdentifier) {
		return environment.getSubmodel(aasIdentifier, submodelIdentifier);
	}

	@Override
	public Optional<AssetAdministrationShell> getAssetAdministrationShell(String identifier) {
		return environment.getAssetAdministrationShell(identifier);
	}

	@Override
	public AssetAdministrationShell setAssetAdministrationShell(String aasIdentifier, AssetAdministrationShell theShell) {
		environment.addAssetAdministrationShell(aasIdentifier, theShell);
		return theShell;
	}

	@Override
	public Optional<ConceptDescription> getConceptDescription(String identifier) {
		return environment.getConceptDescription(identifier);
	}

	@Override
	public boolean deleteAssetAdministrationShellById(String identifier) {
		return environment.deleteAssetAdministrationShell(identifier);
	}

	@Override
	public boolean deleteSubmodelReference(String aasIdentifier, Reference ref) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T extends Referable> Optional<T> resolve(Reference reference, Class<T> type) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public List<AssetAdministrationShell> getAllAssetAdministrationShells() {
		return environment.getAssetAdministrationShells();
	}

	@Override
	public boolean deleteSubmodelElement(String aasIdentifier, String submodelIdentifier, String path) {
		Optional<Submodel> submodel = environment.getSubmodel(aasIdentifier, submodelIdentifier);
		if ( submodel.isPresent()) {
			SubmodelHelper helper = new SubmodelHelper(submodel.get());
			Optional<SubmodelElement> deleted = helper.removeSubmodelElementAt(path);
			deleted.ifPresent(new Consumer<SubmodelElement>() {

				@Override
				public void accept(SubmodelElement t) {
					submodelElementRemoved(aasIdentifier, submodelIdentifier, path, t);
					
				}
			});
			return deleted.isPresent();
		}
		return false;
	}

	@Override
	public SubmodelElement setSubmodelElement(String aasIdentifier, String submodelIdentifier, String idShortPath,
			SubmodelElement body) {
		Optional<Submodel> submodel = environment.getSubmodel(aasIdentifier, submodelIdentifier);
		if ( submodel.isPresent()) {
			
			SubmodelHelper helper = new SubmodelHelper(submodel.get());
			Optional<SubmodelElement> oldElement = helper.removeSubmodelElementAt(idShortPath);
			oldElement.ifPresent(new Consumer<SubmodelElement>() {

				@Override
				public void accept(SubmodelElement t) {
					submodelElementRemoved(aasIdentifier, submodelIdentifier, idShortPath, t);
					
				}
			});

			Optional<SubmodelElement> added = helper.setSubmodelElementAt(idShortPath, body);
			added.ifPresent(new Consumer<SubmodelElement>() {

				@Override
				public void accept(SubmodelElement t) {
					submodelElementAdded(aasIdentifier, submodelIdentifier, idShortPath, t);
					
				}
			});
			if ( added.isPresent()) {
				return added.get();
			}
			return null;
		}
		return null;
	}

	@Override
	public Optional<SubmodelElement> getSubmodelElement(String aasIdentifier, String submodelIdentifier, String path) {
		Optional<Submodel> submodel = environment.getSubmodel(aasIdentifier, submodelIdentifier);
		if ( submodel.isPresent()) {
			return new SubmodelHelper(submodel.get()).getSubmodelElementAt(path);
		}
		return Optional.empty();
	}

	@Override
	public Submodel setSubmodel(String aasIdentifier, String submodelIdentifier, Submodel submodel) {
		Optional<AssetAdministrationShell> shell = environment.getAssetAdministrationShell(aasIdentifier);
		if ( shell.isPresent()) {
			AssetAdministrationShell theShell = shell.get();
			Optional<Reference> ref = ReferenceUtils.getReference(theShell.getSubmodels(), submodelIdentifier, KeyTypes.SUBMODEL);
			if (ref.isEmpty()) {
				Reference newRef = AasUtils.toReference(submodel);
				theShell.getSubmodels().add(newRef);
			}
			environment.addSubmodel(submodelIdentifier, submodel);
		}
		return null;
	}

	@Override
	public Optional<Referable> getSubmodelElement(String aasIdentifier, Reference element) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public Object getElementValue(String aasIdentifier, String submodelIdentifier, String path) {
		Optional<Submodel> sub = environment.getSubmodel(aasIdentifier, submodelIdentifier);
		if ( sub.isPresent() ) {
			return new SubmodelHelper(sub.get()).getValueAt(path);
		}

		return new HashMap<String, Object>();
	}

	@Override
	public void setElementValue(String aasIdentifier, String submodelIdentifier, String path, Object value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ConceptDescription setConceptDescription(String cdIdentifier, ConceptDescription conceptDescription) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Reference> getSubmodelReferences(String aasIdentifier) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Reference> setSubmodelReferences(String aasIdentifier, List<Reference> submodels) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Reference> deleteSubmodelReference(String aasIdentifier, String submodelIdentifier) {
		Optional<AssetAdministrationShell> shell = environment.getAssetAdministrationShell(aasIdentifier);
		if ( shell.isPresent()) {
			AssetAdministrationShell theShell = shell.get();
			Optional<Reference> ref = ReferenceUtils.getReference(theShell.getSubmodels(), submodelIdentifier, KeyTypes.SUBMODEL);
			if (ref.isPresent()) {
				theShell.getSubmodels().remove(ref.get());
				return theShell.getSubmodels();
			}
		}
		return Collections.emptyList();
	}

	@Override
	public SubmodelElement setSubmodelElement(String id, String submodelIdentifier, SubmodelElement element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> invokeOperation(String id, String base64Decode, String path,
			Map<String, Object> parameterMap) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void submodelElementRemoved(String aasIdentifier, String submodelIdentifier, String path, SubmodelElement element) {
		if ( Property.class.isInstance(element) || RelationshipElement.class.isInstance(element)) {
			listeners.forEach(new Consumer<ModelListener>() {

				@Override
				public void accept(ModelListener t) {
					t.propertyRemoved(path);
					
				}
			});
		}
		
	}
	private void submodelElementAdded(String aasIdentifier, String submodelIdentifier, String path, SubmodelElement element) {
		if ( Property.class.isInstance(element) || RelationshipElement.class.isInstance(element)) {
			listeners.forEach(new Consumer<ModelListener>() {

				@Override
				public void accept(ModelListener t) {
					t.submodelElementCreated(path, element);
					
				}
			});
		}
	}

	@Override
	public void setValueConsumer(String aasIdentifier, String submodelIdentifier, String path, Consumer<String> consumer) {
		Optional<Submodel> sub = environment.getSubmodel(aasIdentifier, submodelIdentifier);
		if ( sub.isPresent()) {
			SubmodelHelper helper = new SubmodelHelper(sub.get());
			Optional<Property> property = helper.getSubmodelElementAt(path, Property.class);
			if ( property.isPresent()) {
				Property theProp = property.get();
				if (! InstanceProperty.class.isInstance(theProp)) {
					// need to exchange the default property
					theProp = new InstanceProperty(theProp);
					helper.setSubmodelElementAt(path, theProp);
				}
				if ( InstanceProperty.class.isInstance(theProp)) {
					InstanceProperty iProp = InstanceProperty.class.cast(theProp);
					iProp.consumer(consumer);
				}
				
			}
		}
	}

	@Override
	public void setValueSupplier(String aasIdentifier, String submodelIdentifier, String path, Supplier<String> supplier) {
		Optional<Submodel> sub = environment.getSubmodel(aasIdentifier, submodelIdentifier);
		if ( sub.isPresent()) {
			SubmodelHelper helper = new SubmodelHelper(sub.get());
			Optional<Property> property = helper.getSubmodelElementAt(path, Property.class);
			if ( property.isPresent()) {
				Property theProp = property.get();
				if (! InstanceProperty.class.isInstance(theProp)) {
					// need to exchange the default property
					theProp = new InstanceProperty(theProp);
					helper.setSubmodelElementAt(path, theProp);
				}
				if ( InstanceProperty.class.isInstance(theProp)) {
					InstanceProperty iProp = InstanceProperty.class.cast(theProp);
					iProp.supplier(supplier);
				}
				
			}
		}
		// 
		
	}

	@Override
	public void setOperationFunction(String aasIdentifier, String submodelIdentifier, String path,
			Function<Map<String,Object>, Object> operation) {
		Optional<Submodel> sub = environment.getSubmodel(aasIdentifier, submodelIdentifier);
		if ( sub.isPresent()) {
			SubmodelHelper helper = new SubmodelHelper(sub.get());
			Optional<Operation> property = helper.getSubmodelElementAt(path, Operation.class);
			if ( property.isPresent()) {
				Operation theProp = property.get();
				if (! InstanceProperty.class.isInstance(theProp)) {
					// need to exchange the default property
					theProp = new InstanceOperation(theProp);
					helper.setSubmodelElementAt(path, theProp);
				}
				if ( InstanceOperation.class.isInstance(theProp)) {
					InstanceOperation iProp = InstanceOperation.class.cast(theProp);
					iProp.function(operation);
				}
				
			}
		}
		
	}}
