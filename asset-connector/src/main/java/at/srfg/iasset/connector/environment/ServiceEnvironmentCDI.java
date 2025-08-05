package at.srfg.iasset.connector.environment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShellDescriptor;
import org.eclipse.digitaltwin.aas4j.v3.model.ConceptDescription;
import org.eclipse.digitaltwin.aas4j.v3.model.Endpoint;
import org.eclipse.digitaltwin.aas4j.v3.model.Key;
import org.eclipse.digitaltwin.aas4j.v3.model.KeyTypes;
import org.eclipse.digitaltwin.aas4j.v3.model.Operation;
import org.eclipse.digitaltwin.aas4j.v3.model.Referable;
import org.eclipse.digitaltwin.aas4j.v3.model.Reference;
import org.eclipse.digitaltwin.aas4j.v3.model.ReferenceTypes;
import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelDescriptor;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElement;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import at.srfg.iasset.connector.api.OperationInvocationHandler;
import at.srfg.iasset.connector.component.endpoint.RepositoryConnection;
import at.srfg.iasset.repository.api.exception.NotFoundException;
import at.srfg.iasset.repository.api.model.ExecutionState;
import at.srfg.iasset.repository.api.model.Message;
import at.srfg.iasset.repository.api.model.MessageType;
import at.srfg.iasset.repository.component.ModelListener;
import at.srfg.iasset.repository.component.Persistence;
import at.srfg.iasset.repository.component.ServiceEnvironment;
import at.srfg.iasset.repository.connectivity.ConnectionProvider;
import at.srfg.iasset.repository.model.custom.InstanceOperation;
import at.srfg.iasset.repository.model.helper.ValueHelper;
import at.srfg.iasset.repository.model.helper.payload.PayloadValueHelper;
import at.srfg.iasset.repository.model.helper.value.SubmodelElementValue;
import at.srfg.iasset.repository.model.helper.value.exception.ValueMappingException;
import at.srfg.iasset.repository.model.helper.visitor.EventElementCollector;
import at.srfg.iasset.repository.model.helper.visitor.OperationCollector;
import at.srfg.iasset.repository.model.helper.visitor.SemanticLookupVisitor;
import at.srfg.iasset.repository.model.helper.visitor.SubmodelElementCollector;
import at.srfg.iasset.repository.model.operation.OperationInvocation;
import at.srfg.iasset.repository.model.operation.OperationRequest;
import at.srfg.iasset.repository.model.operation.OperationRequestValue;
import at.srfg.iasset.repository.model.operation.OperationResult;
import at.srfg.iasset.repository.model.operation.OperationResultValue;
import at.srfg.iasset.repository.model.operation.exception.OperationInvocationException;
import at.srfg.iasset.repository.utils.ReferenceUtils;
import at.srfg.iasset.repository.utils.SubmodelUtils;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.InternalServerErrorException;

@ApplicationScoped
public class ServiceEnvironmentCDI implements ServiceEnvironment {
	@Inject
	private Persistence storage;
	
	@Inject
	private ObjectMapper objectMapper;
	
	@Inject
	private ChangeProvider changeProvider;
	
	@Inject
	private RepositoryConnection repository;
	

	@PostConstruct
	private void init() {
		
	}
	/**
	 * Add a {@link ModelListener} to the local environment
	 * @param listener
	 */
	public void addModelListener(ModelListener listener) {
		changeProvider.addModelListener(listener);
	}
	/**
	 * Remove a {@link ModelListener} from the local environment
	 * @param listener
	 */
	public void removeModelListener(ModelListener listener) {
		changeProvider.removeModelListener(listener);
	}

	@Override
	public Optional<Submodel> getSubmodel(String aasIdentifier, String submodelIdentifier) {
		Optional<AssetAdministrationShell> shell = storage.findAssetAdministrationShellById(aasIdentifier);
		if ( shell.isPresent()) {
			if ( ReferenceUtils.extractReferenceFromList(shell.get().getSubmodels(), submodelIdentifier, KeyTypes.SUBMODEL).isPresent() ) {
				return storage.findSubmodelById(submodelIdentifier);
			}		
		}
		return Optional.empty();
	}

	@Override
	public Optional<Submodel> getSubmodel(String submodelIdentifier) {
		return storage.findSubmodelById(submodelIdentifier).or(new Supplier<Optional<? extends Submodel>>() {

			@Override
			public Optional<? extends Submodel> get() {
				// TODO check repository connection
				Optional<Submodel> fromRemote = repository.getSubmodel(submodelIdentifier);
				if (fromRemote.isPresent()) {
					return Optional.of(replaceSubmodel(submodelIdentifier, fromRemote.get()));
//					return Optional.of(storage.persist(fromRemote.get()));
				}
				return Optional.empty();
			}
		});
	}
	private Submodel replaceSubmodel(String submodelIdentifier, Submodel submodel) {
		Optional<Submodel> existing = storage.findSubmodelById(submodelIdentifier);

		existing.ifPresent(new Consumer<Submodel>() {
			
			@Override
			public void accept(Submodel t) {
				changeProvider.notifyDeletion(submodel, "", t);
			}
		});
		submodel.setId(submodelIdentifier);
		Submodel stored = storage.persist(submodel);
		changeProvider.notifyCreation(submodel, "", stored);
		return stored;
	}

	@Override
	public Optional<AssetAdministrationShell> getAssetAdministrationShell(String identifier) {
		return storage.findAssetAdministrationShellById(identifier);
	}

	@Override
	public AssetAdministrationShell setAssetAdministrationShell(String aasIdentifier,
			AssetAdministrationShell theShell) {
		theShell.setId(aasIdentifier);
		return storage.persist(theShell);
	}

	@Override
	public Optional<ConceptDescription> getConceptDescription(String identifier) {
		return storage.findConceptDescriptionById(identifier)
				.or(new Supplier<Optional<ConceptDescription>>() {
					/**
					 * Obtain the requested {@link ConceptDescription} from the repository!
					 */
					@Override
					public Optional<ConceptDescription> get() {
						Optional<ConceptDescription> fromRepo = repository.getConceptDescription(identifier);
						if ( fromRepo.isPresent()) {
							// 
							return Optional.of(storage.persist(fromRepo.get()));
							
						}
						return Optional.empty();
					}});
	}

	@Override
	public boolean deleteAssetAdministrationShellById(String identifier) {
		storage.deleteAssetAdministrationShellById(identifier);
		return true;
	}
	public boolean deleteSubmodel(String aasIdentifier, String submodelIdentifier) {
		Optional<Submodel> toDelete = getSubmodel(aasIdentifier, submodelIdentifier);
		if ( toDelete.isPresent() ) {
			storage.deleteSubmodelById(submodelIdentifier);
			// TODO: handle event
			changeProvider.notifyDeletion(toDelete.get(), "", toDelete.get());
			return true;
		}
		return false;
	}
	@Override
	public boolean deleteSubmodelReference(String aasIdentifier, Reference ref) {
		throw new UnsupportedOperationException("Not yet implemented!");
	}

	@Override
	public <T extends Referable> Optional<T> resolve(Reference reference, Class<T> type) {
		Optional<Referable> ref = resolve(reference);
		if ( ref.isPresent() && type.isInstance(ref.get()) ) {
			return Optional.of(type.cast(ref.get()));
		}
		return Optional.empty();
	}


	@Override
	public Optional<Referable> resolve(Reference reference) {
		if ( reference != null) {
			Iterator<Key> keyIterator = reference.getKeys().iterator();
			if ( keyIterator.hasNext()) {
				Key rootKey = keyIterator.next();
				KeyTypes keyType = rootKey.getType();
				switch(keyType) {
				case SUBMODEL:
					Optional<Submodel> keySub = getSubmodel(rootKey.getValue());
					if ( keySub.isPresent()) {
						if ( keyIterator.hasNext()) {
							Optional<SubmodelElement> elem = SubmodelUtils.resolveKeyPath(keySub.get(), keyIterator);
							if ( elem.isPresent() )
								return Optional.of(elem.get());
						}
						return Optional.of(keySub.get());
					}
					break;
				case CONCEPT_DESCRIPTION:
					Optional<ConceptDescription> cDesc = getConceptDescription(rootKey.getValue());
					if ( cDesc.isPresent()) {
						return Optional.of(cDesc.get());
					}
					break;
				case ASSET_ADMINISTRATION_SHELL:
					Optional<AssetAdministrationShell> aas = storage.findAssetAdministrationShellById(rootKey.getValue());
					if ( aas.isPresent()) {
						if (keyIterator.hasNext()) {
							Key submodelKey = keyIterator.next();
							Optional<Submodel> submodel = getSubmodel(rootKey.getValue(), submodelKey.getValue() );
							if ( submodel.isPresent()) {
								if ( keyIterator.hasNext()) {
									Optional<SubmodelElement> elem = SubmodelUtils.resolveKeyPath(submodel.get(), keyIterator);
									if ( elem.isPresent() )
										return Optional.of(elem.get());
								}
								return Optional.of(submodel.get());
							}
						}
						return Optional.of(aas.get());
					}
					break;
				case GLOBAL_REFERENCE:
					return Optional.empty();
				default:
					throw new IllegalArgumentException("Provided reference points to a non-identifiable element!");
				}
			}
		}
		
		return Optional.empty();
	}

	@Override
	public <T> Optional<T> resolveValue(Reference reference, String path, Class<T> type) {
		Optional<Referable> element = resolve(reference);
		if (element.isPresent()) {
			Referable referable = element.get();
			if (SubmodelElement.class.isInstance(referable)) {
				Object elementValue = SubmodelUtils.getSubmodelElementValue(SubmodelElement.class.cast(referable), path );
				return Optional.of(objectMapper.convertValue(elementValue, type));
			}
		}
		return Optional.empty();
	}

	@Override
	public <T> Optional<T> resolveValue(Reference reference, Class<T> type) {
		// use empty path
		return resolveValue(reference, "", type);
	}

	@Override
	public List<AssetAdministrationShell> getAllAssetAdministrationShells() {
		return storage.getAssetAdministrationShells();
	}

	@Override
	public boolean deleteSubmodelElement(String aasIdentifier, String submodelIdentifier, String path) {
		Optional<Submodel> submodel = getSubmodel(aasIdentifier, submodelIdentifier);
		if ( submodel.isPresent()) {
			Optional<SubmodelElement> deleted = SubmodelUtils.removeSubmodelElementAt(submodel.get(),path);
			deleted.ifPresent(new Consumer<SubmodelElement>() {

				@Override
				public void accept(SubmodelElement t) {
					// TODO: Handle Events
					changeProvider.notifyDeletion(submodel.get(), path, t);
				}
			});
			return deleted.isPresent();
		}
		return false;
	}

	@Override
	public SubmodelElement setSubmodelElement(String aasIdentifier, String submodelIdentifier, String idShortPath,
			SubmodelElement body) {
		Optional<Submodel> submodel = getSubmodel(aasIdentifier, submodelIdentifier);
		if ( submodel.isPresent()) {
			// 
			
			Optional<SubmodelElement> oldElement = SubmodelUtils.removeSubmodelElementAt(submodel.get(), idShortPath);
			oldElement.ifPresent(new Consumer<SubmodelElement>() {

				@Override
				public void accept(SubmodelElement t) {
					// TODO: Handle events
					 changeProvider.notifyDeletion(submodel.get(), idShortPath, t);
					
				}
			});

			Optional<SubmodelElement> added = SubmodelUtils.setSubmodelElementAt(submodel.get(),idShortPath, body);
			added.ifPresent(new Consumer<SubmodelElement>() {
				

				@Override
				public void accept(SubmodelElement t) {
					// TODO: Handle creation event
					 changeProvider.notifyCreation(submodel.get(), idShortPath, t);
					
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
		Optional<Submodel> submodel = getSubmodel(aasIdentifier, submodelIdentifier);
		if ( submodel.isPresent()) {
			return SubmodelUtils.getSubmodelElementAt(submodel.get(), path);
		}
		throw new NotFoundException(aasIdentifier, submodelIdentifier, path);
	}
	public <T extends SubmodelElement> Optional<T> getSubmodelElement(String aasIdentifier, String submodelIdentifier, String path, Class<T> clazz) {
		Optional<SubmodelElement> element = getSubmodelElement(aasIdentifier,  submodelIdentifier, path);
		if ( element.isPresent() ) {
			if ( clazz.isInstance(element.get())) {
				return Optional.of(clazz.cast(element.get()));
			}
		}
		return Optional.empty();
	}

	@Override
	public Optional<SubmodelElement> getSubmodelElement(String submodelIdentifier, String path) {
		Optional<Submodel> submodel = getSubmodel(submodelIdentifier);
		if ( submodel.isPresent()) {
			return SubmodelUtils.getSubmodelElementAt(submodel.get(), path);
		}
		return Optional.empty();	}

	@Override
	public Submodel setSubmodel(String aasIdentifier, String submodelIdentifier, Submodel submodel) {
		Optional<AssetAdministrationShell> shell = getAssetAdministrationShell(aasIdentifier);
		if ( shell.isPresent()) {
			AssetAdministrationShell theShell = shell.get();
			Optional<? extends Reference> ref = ReferenceUtils.extractReferenceFromList(theShell.getSubmodels(), submodelIdentifier, KeyTypes.SUBMODEL);
			if (ref.isEmpty()) {
				Reference newRef = ReferenceUtils.toReference(submodel);
				theShell.getSubmodels().add(newRef);
			}
			Optional<Submodel> existing = storage.findSubmodelById(submodelIdentifier);
			existing.ifPresent(new Consumer<Submodel>() {
				// submodel is to be replaced. trigger a deletion message to listeners
				
				@Override
				public void accept(Submodel t) {
					 changeProvider.notifyDeletion(submodel, "", t);
				}
			});
			
			submodel.setId(submodelIdentifier);
			Submodel stored = storage.persist(submodel);
			// handle event
			changeProvider.notifyCreation(submodel, "", stored);
			return stored;
		}

		return null;	
	}
	@Override
	public Submodel setSubmodel(String submodelIdentifier, Submodel submodel) {
		// be sure to use the proper id
		submodel.setId(submodelIdentifier);
		Submodel stored = storage.persist(submodel);
		// handle event
		changeProvider.notifyCreation(submodel, "", stored);
		return stored;
	}

	@Override
	public Optional<Referable> getSubmodelElement(AssetAdministrationShell aasIdentifier, Reference element) {
		// TODO implement
		return Optional.empty();
	}

	@Override
	public SubmodelElementValue getElementValue(String aasIdentifier, String submodelIdentifier, String path) {
		Optional<Submodel> sub = getSubmodel(aasIdentifier, submodelIdentifier);
		if ( sub.isPresent() ) {
			return SubmodelUtils.getValueAt(sub.get(), path);
		}

		return null;	
	}

	@Override
	public SubmodelElementValue getElementValue(String submodelIdentifier, String path) {
		Optional<SubmodelElement> element = getSubmodelElement(submodelIdentifier, path);
		if ( element.isPresent()) {
			try {
				return ValueHelper.toValue(element.get());
			} catch (ValueMappingException e) {
				throw new InternalServerErrorException(e);
			}
		}
//		Optional<Submodel> submodel = getSubmodel(submodelIdentifier);
//		if ( submodel.isPresent()) {
//			return SubmodelUtils.getValueAt(submodel.get(),path);
//		}
		throw new NotFoundException(submodelIdentifier, path);
	}

	@Override
	public SubmodelElementValue getElementValue(Reference reference) {
		Optional<SubmodelElement> referenced = resolve(reference, SubmodelElement.class);
		if ( referenced.isPresent() ) {
			try {
				return ValueHelper.toValue(referenced.get());
			} catch (ValueMappingException e) {
				throw new InternalServerErrorException(e);

			}
		}
		throw new NotFoundException(String.format("Referenced Element (%s) not found!", 
				PayloadValueHelper.toValue(reference)));
	}

	@Override
	public Optional<Referable> getSubmodelElement(Reference reference) {
		return resolve(reference);
	}

	@Override
	public void setElementValue(String aasIdentifier, String submodelIdentifier, String path, Object value) {
		Optional<Submodel> sub = getSubmodel(aasIdentifier, submodelIdentifier);
		if ( sub.isPresent() ) {
			// make a json node out of it
			JsonNode node = objectMapper.valueToTree(value);
			Optional<SubmodelElement> element = SubmodelUtils.setValueAt(sub.get(),path, node);
			if ( element.isPresent()) {
				// TODO: Handle Events
				changeProvider.notifyChange(sub.get(), path, element.get());
			}
		}

	}

	@Override
	public ConceptDescription setConceptDescription(String cdIdentifier, ConceptDescription conceptDescription) {
		conceptDescription.setId(cdIdentifier);
		return storage.persist(conceptDescription);
	}

	@Override
	public List<Reference> getSubmodelReferences(String aasIdentifier) {
		Optional<AssetAdministrationShell> optShell = getAssetAdministrationShell(aasIdentifier);
		if (optShell.isPresent()) {
			return optShell.get().getSubmodels();
		}
		return Collections.emptyList();
	}

	@Override
	public List<Reference> setSubmodelReferences(String aasIdentifier, List<Reference> submodels) {
		// TODO: check all references point to submodel
		Optional<AssetAdministrationShell> optShell = getAssetAdministrationShell(aasIdentifier);
		if (optShell.isPresent()) {
			optShell.get().setSubmodels(submodels);
			return submodels;
		}
		return Collections.emptyList();
	}

	@Override
	public List<Reference> deleteSubmodelReference(String aasIdentifier, String submodelIdentifier) {
		Optional<AssetAdministrationShell> shell = getAssetAdministrationShell(aasIdentifier);
		if ( shell.isPresent()) {
			AssetAdministrationShell theShell = shell.get();
			Optional<? extends Reference> ref = ReferenceUtils.extractReferenceFromList(theShell.getSubmodels(), submodelIdentifier, KeyTypes.SUBMODEL);
			if (ref.isPresent()) {
				theShell.getSubmodels().remove(ref.get());
				return theShell.getSubmodels();
			}
		}
		return Collections.emptyList();	
	}

	@Override
	public SubmodelElement setSubmodelElement(String aasIdentifier, String submodelIdentifier,
			SubmodelElement element) {
		// TODO implement or remove when not needed
		return null;
	}

	@Override
	public OperationResult invokeOperation(String aasIdentifier, String submodelIdentifier,
			String path, OperationRequest parameterMap) {
		Optional<Submodel> sub = getSubmodel(aasIdentifier, submodelIdentifier);
		if ( sub.isPresent()) {
			Optional<Operation> operation = SubmodelUtils.getSubmodelElementAt(sub.get(), path,Operation.class);
			if ( operation.isPresent() ) {
				if (InstanceOperation.class.isInstance(operation.get())) {
					InstanceOperation instanceOperation = InstanceOperation.class.cast(operation.get());
					// create the operation invocation
					OperationInvocationHandler invocation = new OperationInvocationHandler(instanceOperation, this, this.objectMapper);
					// apply the incoming parameters
					invocation.applyOperationRequest(parameterMap);
					// invoke the operation
					try {
						if ( instanceOperation.callback().execute(invocation) ) {
							// create the OperationResultValue object
							return invocation.getOperationResult(true);
						}
						else {
							return invocation.getOperationResult(false);
						}
					} catch (OperationInvocationException e) {
						OperationResult res = invocation.getOperationResult(false);
						res.setExecutionState(ExecutionState.FAILED);
						res.setSuccess(false);
						res.addMessagesItem(new Message()
									.messageType(MessageType.EXCEPTION)
									.text(e.getLocalizedMessage()));
						return res;
					}
				}
			}
		}
		return null;
	}
	
	@Override
	public OperationResultValue invokeOperationValue(String aasIdentifier, String submodelIdentifier,
			String path, OperationRequestValue parameterMap) {
//		getSubmodelElement(aasIdentifier, submodelIdentifier, path, Operation.class);
		Optional<Operation> theOperation = getSubmodelElement(aasIdentifier, submodelIdentifier, path, Operation.class);
		if ( theOperation.isPresent()) {
			if (InstanceOperation.class.isInstance(theOperation.get())) {
				InstanceOperation instanceOperation = InstanceOperation.class.cast(theOperation.get());
				// create the operation invocation
				OperationInvocationHandler invocation = new OperationInvocationHandler(instanceOperation, this, this.objectMapper);
				// apply the incoming parameters
				invocation.applyOperationRequestValue(parameterMap);
				// invoke the operation
				try {
					if ( instanceOperation.callback().execute(invocation) ) {
						// create the OperationResultValue object with success true
						return invocation.getOperationResultValue(true);
					}
					else {
						return invocation.getOperationResultValue(false);
					}
				} catch (OperationInvocationException e) {
					OperationResultValue res = invocation.getOperationResultValue(false);
					res.setExecutionState(ExecutionState.FAILED);
					res.setSuccess(false);
					res.addMessagesItem(new Message()
								.messageType(MessageType.EXCEPTION)
								.text(e.getLocalizedMessage()));
					return res;
				}

			}
		}
		// the operation is not actively supported 
		return null;
	}
//	@Override
//	public Object invokeOperation(Reference operation, Object parameter) {
//		Optional<Operation> theOperation = resolve(operation, Operation.class);
//		if ( theOperation.isPresent()) {
//			if (InstanceOperation.class.isInstance(theOperation.get())) {
//				InstanceOperation instanceOperation = InstanceOperation.class.cast(theOperation.get());
//				return instanceOperation.invoke(parameter);
//			}
//		}
//		return null;
//	}

	@Override
	public <T extends SubmodelElement> List<T> getSubmodelElements(String aasIdentifier, String submodelIdentifier,
			Reference semanticId, Class<T> clazz) {
		Optional<Submodel> sub = getSubmodel(aasIdentifier, submodelIdentifier);
		if ( sub.isPresent()) {
			return new EventElementCollector().collect(sub.get()).stream()
				.filter(new Predicate<SubmodelElement>() {

					@Override
					public boolean test(SubmodelElement t) {
						if ( t.getSupplementalSemanticIds() != null && !(t.getSupplementalSemanticIds().isEmpty())) {
							if (t.getSupplementalSemanticIds().contains(semanticId)) {
								return true;
							} 
						}
						// 
						if (t.getSemanticId() != null && t.getSemanticId() instanceof Reference) {
							Reference modelReference = (Reference) t.getSemanticId();
							if ( semanticId.equals(modelReference.getReferredSemanticId())) {
								return true;
							}
						}
						return t.getSemanticId().equals(semanticId);
					}
				})
				.map(new Function<SubmodelElement, T>() {

					@Override
					public T apply(SubmodelElement t) {
						return clazz.cast(t);
					}})
				.collect(Collectors.toList());
		}
		return new ArrayList<>();
	}

	@Override
	public <T> T getElementValue(String submodelIdentifier, String path, Class<T> clazz) {
		Object value = getElementValue(submodelIdentifier, path);
		return objectMapper.convertValue(value, clazz);
	}
	@Override
	public <T> T getElementValue(String aasIdentifier, String submodelIdentifier, String path, Class<T> clazz) {
		Object value = getElementValue(aasIdentifier, submodelIdentifier, path);
		return objectMapper.convertValue(value, clazz);

	}

	@Override
	@Deprecated
	public String getConfigProperty(String key) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public <T extends SubmodelElement> Optional<T> getSubmodelElement(Reference reference, Class<T> clazz) {
		// only model references are allowed
		if (reference.getType() == ReferenceTypes.MODEL_REFERENCE) {
			return resolve(reference, clazz);		
			
		}
		// TODO: in case it is a global reference, we will need to search the storage 
		// for an element !!
		Optional<AssetAdministrationShellDescriptor> descriport = repository.findImplementation(ReferenceUtils.firstKeyValue(reference));
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
	public void registerAssetAdministrationShell(AssetAdministrationShellDescriptor aasDescriptor) {
		repository.register(aasDescriptor);
	}
	@Override
	public void unregisterAssetAdministrationShell(String aasIdentifier) {
		repository.unregister(aasIdentifier);
	}
	
	private Optional<OperationInvocation> findLocalImplementation(String semanticId) {
		for (Submodel submodel : storage.getSubmodels()) {
			Set<Operation> operations = new OperationCollector().collect(submodel, ReferenceUtils.asGlobalReference(semanticId));
			if (! operations.isEmpty()) {
				for (Operation operation : operations ) {
					if ( InstanceOperation.class.isInstance(operation)) {
						return Optional.of(new OperationInvocationHandler(InstanceOperation.class.cast(operation), this, objectMapper));
					}
				}
				// 
			}
		}
		return Optional.empty();
	}
	@Override
	public Optional<OperationInvocation> getImplementation(String semanticId) {
		Optional<OperationInvocation> localImplementation = findLocalImplementation(semanticId);
		if ( localImplementation.isPresent()) {
			return localImplementation;
		}
		Optional<AssetAdministrationShellDescriptor> implementor = repository.findImplementation(semanticId);
		if ( implementor.isPresent() ) {
			//
			// extract endpoint
			Reference semanticReference = ReferenceUtils.asGlobalReference(semanticId);
			
			AssetAdministrationShellDescriptor aasDescriptor = implementor.get();
			Optional<Endpoint> endpoint = aasDescriptor.getEndpoints().stream().filter(new Predicate<Endpoint>() {
				@Override
				public boolean test(Endpoint t) {
					// check for proper endpoint interface
					if ( t.get_interface().equals("AAS-3.0_ITWIN")) {
						if ( t.getProtocolInformation().getHref()!= null) {
							return true;
						}
					}
					// not a valid endpoint
					return false;
				}
			}).findFirst();
			// OK with the endpoint for registry interface
			if ( endpoint.isPresent() ) {
				
			
				Optional<SubmodelDescriptor> submodelImplementing = aasDescriptor.getSubmodelDescriptors().stream().filter(new Predicate<SubmodelDescriptor>() {
	
					@Override
					public boolean test(SubmodelDescriptor t) {
						// check for proper interface!
						// 
						return t.getSupplementalSemanticId().stream().anyMatch(new Predicate<Reference>() {
	
							@Override
							public boolean test(Reference t) {
								return semanticReference.equals(t);
							}
						});
					}
	
					}).findFirst();
			
				if ( submodelImplementing.isPresent() ) {
					SubmodelDescriptor submodelDescriptor = (SubmodelDescriptor) submodelImplementing.get();
					
						ConnectionProvider connection = ConnectionProvider.getConnection(endpoint.get().getProtocolInformation().getHref());
						
						
						Submodel remoteSubmodel = connection.getShellInterface().getSubmodel(submodelDescriptor.getId());
						if ( remoteSubmodel != null) {
							Set<Operation> operations = new OperationCollector().collect(remoteSubmodel, semanticReference);
							// 
							for ( Operation operation : operations) {
								if (! operations.isEmpty()) {
									String path = new SubmodelElementCollector().getPath("", remoteSubmodel, operation);
									return Optional.of(new OperationInvocationHandler(connection.getShellInterface(), submodelDescriptor.getId(), path, operation, this, objectMapper));
								}
							}
						}							
					}
				}
			
			// search for operation / event equipped with the semanticId
			// extract the reference from the endpoint
			// 
//			AssetAdministrationShellDescriptor descriptor 
		}
		return Optional.empty();
	}

}
