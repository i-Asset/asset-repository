package at.srfg.iasset.repository.service.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShellDescriptor;
import org.eclipse.digitaltwin.aas4j.v3.model.ConceptDescription;
import org.eclipse.digitaltwin.aas4j.v3.model.Endpoint;
import org.eclipse.digitaltwin.aas4j.v3.model.Key;
import org.eclipse.digitaltwin.aas4j.v3.model.KeyTypes;
import org.eclipse.digitaltwin.aas4j.v3.model.ModelReference;
import org.eclipse.digitaltwin.aas4j.v3.model.Property;
import org.eclipse.digitaltwin.aas4j.v3.model.Referable;
import org.eclipse.digitaltwin.aas4j.v3.model.Reference;
import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import at.srfg.iasset.repository.api.IAssetAdministrationShellInterface;
import at.srfg.iasset.repository.api.exception.NotFoundException;
import at.srfg.iasset.repository.component.ModelListener;
import at.srfg.iasset.repository.component.Persistence;
import at.srfg.iasset.repository.component.ServiceEnvironment;
import at.srfg.iasset.repository.connectivity.ConnectionProvider;
import at.srfg.iasset.repository.model.helper.value.SubmodelElementValue;
import at.srfg.iasset.repository.model.helper.visitor.SemanticLookupVisitor;
import at.srfg.iasset.repository.model.operation.OperationInvocation;
import at.srfg.iasset.repository.model.operation.OperationRequest;
import at.srfg.iasset.repository.model.operation.OperationRequestValue;
import at.srfg.iasset.repository.model.operation.OperationResult;
import at.srfg.iasset.repository.model.operation.OperationResultValue;
import at.srfg.iasset.repository.utils.ReferenceUtils;
import at.srfg.iasset.repository.utils.SubmodelUtils;
import jakarta.validation.Valid;

@Service
public class RepositoryEnvironment implements ServiceEnvironment {
	@Autowired
	private Persistence storage;
	
	@Autowired
	private ObjectMapper aasMapper;
	
	@Override
	public Optional<Submodel> getSubmodel(String aasIdentifier, String submodelIdentifier) {
		Optional<AssetAdministrationShell> theShell = storage.findAssetAdministrationShellById(aasIdentifier);
		if ( theShell.isPresent()) {
			if ( hasSubmodelReference(theShell.get(), submodelIdentifier) ) {
				return storage.findSubmodelById(submodelIdentifier);
			}
		}
		return Optional.empty();
	}

	private boolean hasSubmodelReference(AssetAdministrationShell theShell, String submodelIdentifier) {
		Optional<ModelReference> submodelRef = theShell.getSubmodels().stream()
				.filter(new Predicate<ModelReference>() {
					@Override
					public boolean test(ModelReference t) {
						if (t.getKeys().size() > 0 ) {
							Key first = t.getKeys().get(0);
							if (KeyTypes.SUBMODEL.equals(first.getType()) 
									&& submodelIdentifier.equalsIgnoreCase(first.getValue())) {
								return true;
							}
						}
						return false;
					}
				})
				.findFirst();
				
		return submodelRef.isPresent();
	}
	@Override
	public Optional<AssetAdministrationShell> getAssetAdministrationShell(String identifier) {
		return storage.findAssetAdministrationShellById(identifier);
	}
	@Override
	public AssetAdministrationShell setAssetAdministrationShell(String aasIdentifier, AssetAdministrationShell theShell) {
		// TODO: decide what to do with aasIdentifier
		return storage.persist(theShell);
	}
	@Override
	public Optional<ConceptDescription> getConceptDescription(String identifier) {
		return storage.findConceptDescriptionById(identifier);
	}
	
	@Override
	public boolean deleteAssetAdministrationShellById(String identifier) {
		storage.deleteAssetAdministrationShellById(identifier);
		return true;
	}
	@Override
	public boolean deleteSubmodelReference(String aasIdentifier, Reference ref) {
		Optional<AssetAdministrationShell> theShell = storage.findAssetAdministrationShellById(aasIdentifier);
		if ( theShell.isPresent() ) {
			return deleteSubmodelReference(theShell.get(), ref);
		}
		return false;
	}
	private boolean deleteSubmodelReference(AssetAdministrationShell theShell, Reference ref) {
		if ( theShell.getSubmodels().remove(ref) ) {
			storage.persist(theShell);
			return true;
		}
		return false;
	}
	@Override
	public <T extends Referable> Optional<T> resolve(Reference reference, Class<T> type) {
		switch(ReferenceUtils.firstKeyType(reference)) {
		case CONCEPT_DESCRIPTION:
			// return the concept description
			Optional<ConceptDescription> cd = storage.findConceptDescriptionById(ReferenceUtils.firstKeyValue(reference));
			if ( cd.isPresent() ) {
				if ( cd.isPresent() && type.isInstance(cd.get())) {
					return Optional.of(type.cast(cd.get())); 
					
				}
			}
			break;
		case SUBMODEL:
			Optional<Submodel> submodel = storage.findSubmodelById(ReferenceUtils.firstKeyValue(reference));
			if ( submodel.isPresent()) {
				Optional<Referable> resolved = SubmodelUtils.resolveReference(submodel.get(), reference);
				if ( resolved.isPresent() && type.isInstance(resolved.get())) {
					return Optional.of(type.cast(resolved.get())); 
					
				}
			}
			break;
		case ASSET_ADMINISTRATION_SHELL:
			Iterator<Key> keyIterator = ReferenceUtils.keyIterator(reference);
			// need to extract the first key (AAS) and the second key (Submodel) ...
			if ( keyIterator != null && keyIterator.hasNext()) {
				Optional<AssetAdministrationShell> shell = storage.findAssetAdministrationShellById(keyIterator.next().getValue());
				if (shell.isPresent() && keyIterator.hasNext() ) {
					// 
					String submodelIdentifier = keyIterator.next().getValue();
					if ( hasSubmodelReference(shell.get(), submodelIdentifier)) {
						// remove the first key from the 
						Reference submodelRef = ReferenceUtils.clone(reference);
						submodelRef.getKeys().remove(0);
						// resolve the remainder (Reference now points to Submodel)
						return resolve(submodelRef,type);
					}
				}
			}
		default:
			break;
		}
		return Optional.empty();
	}
	public Optional<Referable> resolve(Reference reference) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<AssetAdministrationShell> getAllAssetAdministrationShells() {
		return storage.getAssetAdministrationShells();
	}
	@Override
	public boolean deleteSubmodelElement(String aasIdentifier, String submodelIdentifier, String path) {
		Optional<Submodel> theSubmodel = getSubmodel(aasIdentifier, submodelIdentifier);
		if ( theSubmodel.isPresent()) {
			return deleteSubmodelElementByPath(theSubmodel.get(), path);
		}
		return false;
		
	}
	@Override
	public SubmodelElement setSubmodelElement(String aasIdentifier, String submodelIdentifier, String idShortPath,
			SubmodelElement body) {
		Optional<Submodel> theSubmodel = getSubmodel(aasIdentifier, submodelIdentifier);
		if ( theSubmodel.isPresent()) {
			//
			Optional<SubmodelElement> elementAdded = SubmodelUtils.setSubmodelElementAt(theSubmodel.get(),idShortPath, body);
			if (elementAdded.isPresent()) {
				storage.persist(theSubmodel.get());
				return elementAdded.get();
			}
		}
		return null;
		
	}
	/**
	 * Remvoe a {@link SubmodelElement} from the provided {@link Submodel}. The element is 
	 * identified based on it's path!
	 * @param submodel The {@link Submodel} 
	 * @param path The dot-separated path to the element
	 * @return
	 */
	private boolean deleteSubmodelElementByPath(Submodel submodel, String path) {
		Optional<SubmodelElement> deleted = SubmodelUtils.removeSubmodelElementAt(submodel, path);
		if (deleted.isPresent()) {
			storage.persist(submodel);
			return true;
		}
		return false;
	}
	/**
	 * Find a {@link SubmodelElement} in the reference {@link Submodel}. 
	 * The {@link Submodel} must be assigned to the {@link AssetAdministrationShell}
	 * @param aasIdentifier
	 * @param submodelIdentifier
	 * @param path
	 * @return
	 */
	@Override
	public Optional<SubmodelElement> getSubmodelElement(String aasIdentifier,String submodelIdentifier, String path) {
		Optional<Submodel> theSub = getSubmodel(aasIdentifier, submodelIdentifier);
		if ( theSub.isPresent()) {
			return SubmodelUtils.getSubmodelElementAt(theSub.get(), path);
		}
		return Optional.empty();
	}
	/**
	 * Update a submodel in the repository. Maintain the list of assigned repositories with the AAS
	 * @param aasIdentifier
	 * @param submodelIdentifier
	 * @param submodel
	 * @return
	 */
	@Override
	public Submodel setSubmodel(String aasIdentifier, String submodelIdentifier, @Valid Submodel submodel) {
		Optional<AssetAdministrationShell> shell = storage.findAssetAdministrationShellById(aasIdentifier);
		if ( shell.isPresent()) {
			AssetAdministrationShell theShell = shell.get();
			Optional<ModelReference> submodelReference =  theShell.getSubmodels().stream()
					.filter(new Predicate<ModelReference>() {

						@Override
						public boolean test(ModelReference t) {
							return ReferenceUtils.lastKeyType(t).equals(KeyTypes.SUBMODEL)
									&& ReferenceUtils.lastKeyValue(t).equalsIgnoreCase(submodelIdentifier);
						}
					})
					.findAny();
			if (submodelReference.isEmpty()) {
				theShell.getSubmodels().add(ReferenceUtils.toReference(submodel));
				storage.persist(theShell);
			}
			submodel.setId(submodelIdentifier);
			return storage.persist(submodel);
		}
		return null;
	}

	@Override
	public Submodel setSubmodel(String submodelIdentifier, Submodel submodel) {
		submodel.setId(submodelIdentifier);
		return storage.persist(submodel);
	}

	public Optional<Referable> getSubmodelElement(AssetAdministrationShell aasIdentifier, Reference element) {
		// 
		switch(ReferenceUtils.firstKeyType(element)) {
		case ASSET_ADMINISTRATION_SHELL:
			// invalid use ... shell is provided with the first argument
			break;
		case CONCEPT_DESCRIPTION:
			// return the concept description
			Optional<ConceptDescription> cd = storage.findConceptDescriptionById(ReferenceUtils.firstKeyValue(element));
			if ( cd.isPresent() ) {
				return Optional.of(cd.get());
			}
			break;
		case SUBMODEL:
			Optional<Submodel> submodel = storage.findSubmodelById(ReferenceUtils.firstKeyValue(element));
			if ( submodel.isPresent()) {
				if ( hasSubmodelReference(aasIdentifier, submodel.get().getId())) {
					return SubmodelUtils.resolveReference(submodel.get(),element);
				}
			}
			break;
		default:
			break;
		}
		return Optional.empty();
	}
	@Override
	public SubmodelElementValue getElementValue(String aasIdentifier, String submodelIdentifier, String path) {
		Optional<Submodel> submodel = getSubmodel(aasIdentifier, submodelIdentifier);
		
		if ( submodel.isPresent()) {
			return SubmodelUtils.getValueAt(submodel.get(), path);
		}
		return null;
	}
	@Override
	public void setElementValue(String aasIdentifier, String submodelIdentifier, String path, Object value) {
		Optional<SubmodelElement> element = getSubmodelElement(aasIdentifier, submodelIdentifier, path);
		
		if ( element.isPresent() ) {
			
			if (Property.class.isInstance(element.get())) {
				Property.class.cast(element).setValue(value.toString());
			}
		}
		
	}
	@Override
	public ConceptDescription setConceptDescription(String cdIdentifier, ConceptDescription conceptDescription) {
		conceptDescription.setId(cdIdentifier);
		return storage.persist(conceptDescription);
	}
	@Override
	public List<ModelReference> getSubmodelReferences(String aasIdentifier) {
		Optional<AssetAdministrationShell> shell = storage.findAssetAdministrationShellById(aasIdentifier);
		if ( shell.isPresent()) {
			return shell.get().getSubmodels();
		}
		return new ArrayList<ModelReference>();
	}
	@Override
	public List<ModelReference> setSubmodelReferences(String aasIdentifier, List<ModelReference> submodels) {
		Optional<AssetAdministrationShell> theShell = storage.findAssetAdministrationShellById(aasIdentifier);
		if ( theShell.isPresent()) {
			theShell.get().setSubmodels(submodels);
		}
		return null;
	}
	@Override
	public List<ModelReference> deleteSubmodelReference(String aasIdentifier, String submodelIdentifier) {
		Optional<AssetAdministrationShell> theShell = storage.findAssetAdministrationShellById(aasIdentifier);
		if ( theShell.isPresent()) {
			AssetAdministrationShell shell = theShell.get();
			List<ModelReference> remaining = shell.getSubmodels().stream().filter(new Predicate<ModelReference>() {

				@Override
				public boolean test(ModelReference t) {
					return ! submodelIdentifier.equalsIgnoreCase(ReferenceUtils.firstKeyValue(t));
				}
			}).collect(Collectors.toList());
			shell.setSubmodels(remaining);
			storage.persist(shell);
			return remaining;
		}
		return Collections.emptyList();
	}
	@Override
	public SubmodelElement setSubmodelElement(String aasIdentifier, String submodelIdentifier, SubmodelElement element) {
		Optional<Submodel> sub = getSubmodel(aasIdentifier, submodelIdentifier);
		if ( sub.isPresent()) {
			Submodel submodel = sub.get();
			if ( submodel.getSubmodelElements().add(element) ) {
				return element;
			}
		}
		return null;
	}
//	@Override
//	public OperationResultValue invokeOperation(String aasIdentifier, String submodelIdentifier, String path,
//			OperationRequestValue parameterMap) {
//		
//		Optional<AssetAdministrationShellDescriptor> descriptor = storage.findAssetAdministrationShellDescriptorById(aasIdentifier);
//		if ( descriptor.isPresent()) {
//			Optional<Endpoint> endpoint = descriptor.get().getEndpoints().stream().findFirst();
//			if (endpoint.isPresent()) {
//				
//				IAssetAdministrationShellInterface shellConnector = ConnectionProvider.getConnection(endpoint.get().getAddress()).getShellInterface();
//				return shellConnector.invokeOperation(submodelIdentifier, path, parameterMap);
//			}
//		}
//		// TODO: report the absence of the shell!
//		return null;
//	}

//	@Override
//	public Object invokeOperation(Reference operation, Object parameter) {
//		throw new UnsupportedOperationException();
//	}

	@Override
	public <T extends SubmodelElement> List<T> getSubmodelElements(String aasIdentifier, String submodelIdentifier,
			Reference semanticId, Class<T> clazz) {
//		Optional<Submodel> sub = getSubmodel(aasIdentifier, submodelIdentifier);
//		if ( sub.isPresent()) {
//			new EventElementCollector().collect(sub.get());
//		}
		return new ArrayList<T>();
	}

	@Override
	public Optional<Submodel> getSubmodel(String submodelIdentifier) {
		return storage.findSubmodelById(submodelIdentifier);
	}

	@Override
	public Optional<SubmodelElement> getSubmodelElement(String submodelIdentifier, String path) {
		Optional<Submodel> theSub = getSubmodel(submodelIdentifier);
		if ( theSub.isPresent()) {
			return SubmodelUtils.getSubmodelElementAt(theSub.get(), path);
		}
		return Optional.empty();
	}

	@Override
	public SubmodelElementValue getElementValue(String submodelIdentifier, String path) {
		Optional<Submodel> theSub = getSubmodel(submodelIdentifier);
		if ( theSub.isPresent()) {
			return SubmodelUtils.getValueAt(theSub.get(), path);
		}
		return null;
	}
	@Override
	public <T> T getElementValue(String aasIdentifier, String submodelIdentifier, String path, Class<T> clazz) {
		Optional<Submodel> theSub = getSubmodel(aasIdentifier, submodelIdentifier);
		if ( theSub.isPresent()) {
			Object value = SubmodelUtils.getValueAt(theSub.get(), path);
			return aasMapper.convertValue(value, clazz);
		}
		return null;
	}
	@Override
	public <T> T getElementValue(String submodelIdentifier, String path, Class<T> clazz) {
		Optional<Submodel> theSub = getSubmodel(submodelIdentifier);
		if ( theSub.isPresent()) {
			Object value = SubmodelUtils.getValueAt(theSub.get(), path);
			return aasMapper.convertValue(value, clazz);
		}
		return null;
	}

	@Override
	public SubmodelElementValue getElementValue(Reference reference) {
		throw new UnsupportedOperationException("Not yet implemented!");
	}

	@Override
	public Optional<Referable> getSubmodelElement(Reference reference) {
		return Optional.empty();
	}

	@Override
	public <T extends SubmodelElement> Optional<T> getSubmodelElement(Reference reference, Class<T> clazz) {
		// only model references are allowed
		if ( ModelReference.class.isInstance(reference) ) {
			return resolve(reference, clazz);		
			
		}
		// TODO: in case it is a external reference, we will need to search the storage 
		// for an element !!
		for ( Submodel submodel : storage.getSubmodels() ) {
			//
			Optional<T> elemWithRef = new SemanticLookupVisitor(submodel).findElement(reference, clazz);
			if ( elemWithRef.isPresent()) {
				return elemWithRef;
			}
			// not found in this submodel, continue ... 
		}
		return Optional.empty();
	}

	@Override
	public <T> Optional<T> resolveValue(Reference reference, String path, Class<T> type) {
		// TODO IMPLEMENT
		return Optional.empty();
	}

	@Override
	public <T> Optional<T> resolveValue(Reference reference, Class<T> type) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public String getConfigProperty(String key) {
		// TODO IMPLEMENT
		return null;
	}

	@Override
	public void addModelListener(ModelListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeModelListener(ModelListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public OperationResult invokeOperation(String aasIdentifier, String submodelIdentifier, String path,
			OperationRequest parameterMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OperationResultValue invokeOperationValue(String aasIdentifier, String submodelIdentifier, String path,
			OperationRequestValue parameterMap) {
		Optional<AssetAdministrationShellDescriptor> descriptor = storage.findAssetAdministrationShellDescriptorById(aasIdentifier);
		if ( descriptor.isPresent()) {
			Optional<Endpoint> endpoint = descriptor.get().getEndpoints().stream().findFirst();
			if (endpoint.isPresent()) {
				
				IAssetAdministrationShellInterface shellConnector = ConnectionProvider.getConnection(endpoint.get().getProtocolInformation().getHref()).getShellInterface();
				return shellConnector.invokeOperation(submodelIdentifier, path, parameterMap);
			}
		}
		// TODO: Test this is reported to the caller!
		throw new NotFoundException( aasIdentifier, submodelIdentifier, path);
	}

	@Override
	public void registerAssetAdministrationShell(AssetAdministrationShellDescriptor aasIdentifier) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unregisterAssetAdministrationShell(String aasIdentifier) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Optional<OperationInvocation> getImplementation(String semanticId) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}


	
}
