package at.srfg.iasset.repository.persistence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.eclipse.aas4j.v3.dataformat.core.util.AasUtils;
import org.eclipse.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.aas4j.v3.model.ConceptDescription;
import org.eclipse.aas4j.v3.model.Key;
import org.eclipse.aas4j.v3.model.KeyTypes;
import org.eclipse.aas4j.v3.model.Property;
import org.eclipse.aas4j.v3.model.Referable;
import org.eclipse.aas4j.v3.model.Reference;
import org.eclipse.aas4j.v3.model.Submodel;
import org.eclipse.aas4j.v3.model.SubmodelElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.srfg.iasset.repository.component.ServiceEnvironment;
import at.srfg.iasset.repository.model.helper.SubmodelHelper;
import at.srfg.iasset.repository.persistence.service.AssetAdministrationShellRepository;
import at.srfg.iasset.repository.persistence.service.ConceptDescriptionRepository;
import at.srfg.iasset.repository.persistence.service.SubmodelRepository;
import at.srfg.iasset.repository.utils.ReferenceUtils;

@Service
public class MongoEnvironment implements ServiceEnvironment {
	@Autowired
	private ConceptDescriptionRepository conceptDescriptionRepository;
	
	@Autowired
	private SubmodelRepository submodelRepository;
	
	@Autowired
	private AssetAdministrationShellRepository assetAdministrationShellRepository;
	
	@Override
	public Optional<Submodel> getSubmodel(String identifier) {
		return submodelRepository.findById(identifier);
	}
	@Override
	public Optional<Submodel> getSubmodel(String aasIdentifier, String submodelIdentifier) {
		Optional<AssetAdministrationShell> theShell = assetAdministrationShellRepository.findById(aasIdentifier);
		if ( theShell.isPresent()) {
			if ( hasSubmodelReference(theShell.get(), submodelIdentifier) ) {
				return submodelRepository.findById(submodelIdentifier);
			}
		}
		return Optional.empty();
	}

	private boolean hasSubmodelReference(AssetAdministrationShell theShell, String submodelIdentifier) {
		Optional<Reference> submodelRef = theShell.getSubmodels().stream()
				.filter(new Predicate<Reference>() {
					@Override
					public boolean test(Reference t) {
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
		return assetAdministrationShellRepository.findById(identifier);
	}
	@Override
	public AssetAdministrationShell setAssetAdministrationShell(String aasIdentifier, AssetAdministrationShell theShell) {
		// TODO: decide what to do with aasIdentifier
		return assetAdministrationShellRepository.save(theShell);
	}
	@Override
	public Optional<ConceptDescription> getConceptDescription(String identifier) {
		return conceptDescriptionRepository.findById(identifier);
	}
	
	@Override
	public boolean deleteAssetAdministrationShellById(String identifier) {
		assetAdministrationShellRepository.deleteById(identifier);
		return true;
	}
	@Override
	public boolean deleteSubmodelReference(String aasIdentifier, Reference ref) {
		Optional<AssetAdministrationShell> theShell = assetAdministrationShellRepository.findById(aasIdentifier);
		if ( theShell.isPresent() ) {
			return deleteSubmodelReference(theShell.get(), ref);
		}
		return false;
	}
	private boolean deleteSubmodelReference(AssetAdministrationShell theShell, Reference ref) {
		if ( theShell.getSubmodels().remove(ref) ) {
			assetAdministrationShellRepository.save(theShell);
			return true;
		}
		return false;
	}
	@Override
	public <T extends Referable> Optional<T> resolve(Reference reference, Class<T> type) {
        if (reference == null || reference.getKeys() == null || reference.getKeys().isEmpty()) {
            return Optional.empty();
        }
        // last element
        for ( Key key : reference.getKeys()) {
        	Class<?> keyType = AasUtils.keyTypeToClass(key.getType());
        	
        }
        int i = reference.getKeys().size() -1;
        if (type != null) { 
        	// obtain the desired class element
            Class<?> actualType = AasUtils.keyTypeToClass(reference.getKeys().get(i).getType());
            if (actualType == null) {
//                log.warn("reference {} could not be resolved as key type has no known class.",
//                        asString(reference));
                return null;
            }
            if (!type.isAssignableFrom(actualType)) {
//                log.warn("reference {} could not be resolved as target type is not assignable from actual type (target: {}, actual: {})",
//                        asString(reference), type.getName(), actualType.getName());
                return null;
            }
        }
        
		return Optional.empty();
	}
	@Override
	public List<AssetAdministrationShell> getAllAssetAdministrationShells() {
		return assetAdministrationShellRepository.findAll();
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
			Optional<SubmodelElement> elementAdded = new SubmodelHelper(theSubmodel.get()).setSubmodelElementAt(idShortPath, body);
			if (elementAdded.isPresent()) {
				submodelRepository.save(theSubmodel.get());
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
		SubmodelHelper mySubmodel = new SubmodelHelper(submodel);
		Optional<SubmodelElement> deleted = mySubmodel.removeSubmodelElementAt(path);
		if (deleted.isPresent()) {
			submodelRepository.save(mySubmodel.getSubmodel());
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
			return new SubmodelHelper(theSub.get()).getSubmodelElementAt(path);
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
		Optional<AssetAdministrationShell> shell = assetAdministrationShellRepository.findById(aasIdentifier);
		if ( shell.isPresent()) {
			AssetAdministrationShell theShell = shell.get();
			Optional<Reference> submodelReference =  theShell.getSubmodels().stream()
					.filter(new Predicate<Reference>() {

						@Override
						public boolean test(Reference t) {
							return ReferenceUtils.lastKeyType(t).equals(KeyTypes.SUBMODEL)
									&& ReferenceUtils.lastKeyValue(t).equalsIgnoreCase(submodelIdentifier);
						}
					})
					.findAny();
			if (submodelReference.isEmpty()) {
				theShell.getSubmodels().add(AasUtils.toReference(submodel));
				assetAdministrationShellRepository.save(theShell);
			}
			submodel.setId(submodelIdentifier);
			return submodelRepository.save(submodel);
		}
		return null;
	}

	@Override
	public Optional<Referable> getSubmodelElement(String aasIdentifier, Reference element) {
		// 
		switch(ReferenceUtils.firstKeyType(element)) {
		case CONCEPT_DESCRIPTION:
			// return the concept description
			Optional<ConceptDescription> cd = conceptDescriptionRepository.findById(ReferenceUtils.firstKeyValue(element));
			if ( cd.isPresent() ) {
				return Optional.of(cd.get());
			}
			break;
		case SUBMODEL:
			Optional<Submodel> submodel = submodelRepository.findById(ReferenceUtils.firstKeyValue(element));
			if ( submodel.isPresent()) {
				return new SubmodelHelper(submodel.get()).resolveReference(element);
			}
			break;
		case ASSET_ADMINISTRATION_SHELL:
		default:
			break;
		}
		return Optional.empty();
	}
	@Override
	public Object getElementValue(String aasIdentifier, String submodelIdentifier, String path) {
		// TODO Auto-generated method stub
		Optional<Submodel> submodel = getSubmodel(aasIdentifier, submodelIdentifier);
		
		if ( submodel.isPresent()) {
			return new SubmodelHelper(submodel.get()).getValueAt(path);
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
		return conceptDescriptionRepository.save(conceptDescription);
	}
	@Override
	public List<Reference> getSubmodelReferences(String aasIdentifier) {
		Optional<AssetAdministrationShell> shell = assetAdministrationShellRepository.findById(aasIdentifier);
		if ( shell.isPresent()) {
			return shell.get().getSubmodels();
		}
		return new ArrayList<Reference>();
	}
	@Override
	public List<Reference> setSubmodelReferences(String aasIdentifier, List<Reference> submodels) {
		Optional<AssetAdministrationShell> theShell = assetAdministrationShellRepository.findById(aasIdentifier);
		if ( theShell.isPresent()) {
			theShell.get().setSubmodels(submodels);
		}
		return null;
	}
	@Override
	public List<Reference> deleteSubmodelReference(String aasIdentifier, String submodelIdentifier) {
		Optional<AssetAdministrationShell> theShell = assetAdministrationShellRepository.findById(aasIdentifier);
		if ( theShell.isPresent()) {
			AssetAdministrationShell shell = theShell.get();
			List<Reference> remaining = shell.getSubmodels().stream().filter(new Predicate<Reference>() {

				@Override
				public boolean test(Reference t) {
					return ! submodelIdentifier.equalsIgnoreCase(ReferenceUtils.firstKeyValue(t));
				}
			}).collect(Collectors.toList());
			shell.setSubmodels(remaining);
			assetAdministrationShellRepository.save(shell);
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
	@Override
	public Map<String, Object> invokeOperation(String aasIdentifier, String submodelIdentifier, String path,
			Map<String, Object> parameterMap) {
		// TODO Auto-generated method stub
		Optional<SubmodelElement> operation = getSubmodelElement(aasIdentifier, submodelIdentifier, path);
		// TODO: find the service endpoint and invoke the operation there
		return null;
	}


	
}
