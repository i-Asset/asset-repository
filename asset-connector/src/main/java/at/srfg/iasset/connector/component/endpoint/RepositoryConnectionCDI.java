package at.srfg.iasset.connector.component.endpoint;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShellDescriptor;
import org.eclipse.digitaltwin.aas4j.v3.model.ConceptDescription;
import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElement;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultAssetAdministrationShellDescriptor;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultEndpoint;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultProtocolInformation;

import at.srfg.iasset.connector.component.config.ServiceProvider;
import at.srfg.iasset.repository.model.operation.OperationInvocation;
import at.srfg.iasset.repository.model.operation.OperationInvocationResult;
import at.srfg.iasset.repository.model.operation.OperationRequest;
import at.srfg.iasset.repository.model.operation.OperationRequestValue;
import at.srfg.iasset.repository.model.operation.OperationResult;
import at.srfg.iasset.repository.model.operation.OperationResultValue;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.ws.rs.ServiceUnavailableException;

@Dependent
public class RepositoryConnectionCDI implements RepositoryConnection {
	
	@Inject
	ServiceProvider serviceProvider;
	
	
	@Override
	public boolean register(AssetAdministrationShellDescriptor descriptor) {
		
		serviceProvider.getDirectoryInterface().register(descriptor.getId(), descriptor);
		return true;
	}

	@Override
	public boolean unregister(String aasIdentifier) {
		try {
			serviceProvider.getDirectoryInterface().unregister(aasIdentifier);
			return true;
		} catch (ServiceUnavailableException e) {
			return false;
		}
	}

	@Override
	public AssetAdministrationShell save(AssetAdministrationShell shell) {
		return null;
	}

	@Override
	public Optional<AssetAdministrationShell> getAssetAdministrationShell(String aasIdentifier) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public Optional<Submodel> getSubmodel(String aasIdentifier, String submodelIdentifier) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public Optional<Submodel> getSubmodel(String submodelIdentifier) {
		return Optional.ofNullable(serviceProvider.getSubmodelInterface().getSubmodel(submodelIdentifier));
	}

	@Override
	public Optional<ConceptDescription> getConceptDescription(String conceptIdentifier) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public <T extends SubmodelElement> Optional<T> getSubmodelElement(String aasIdentifier, String submodelIdentifier,
			String path, Class<T> elementClass) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public OperationResult invokeOperation(String aasIdentifier, String submodelIdentifier, String path, OperationRequest parameter) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public OperationResultValue invokeOperation(String aasIdentifier, String submodelIdentifier, String path, OperationRequestValue parameter) {
		// TODO Auto-generated method stub
		return null;
	}
	public OperationInvocationResult invoke(OperationInvocation invocation) {
//		serviceProvider.getRepositoryInterface().ino
		return null;
	}

//	@Override
//	public Optional<AssetAdministrationShellDescriptor> findImplementation(String semanticId) {
//		return Optional.ofNullable( serviceProvider.getDirectoryInterface().lookupBySemanticId(semanticId));	
//	}
	@Override
	public Optional<AssetAdministrationShellDescriptor> findImplementation(String semanticId, String ... additional ) {
		// 
		return Optional.ofNullable( serviceProvider.getDirectoryInterface().lookupBySemanticIds(semanticId, List.of(additional) ));	
	}
}
