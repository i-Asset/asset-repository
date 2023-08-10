package at.srfg.iasset.connector.component.endpoint;

import java.net.URI;
import java.util.Optional;

import org.eclipse.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.aas4j.v3.model.AssetAdministrationShellDescriptor;
import org.eclipse.aas4j.v3.model.ConceptDescription;
import org.eclipse.aas4j.v3.model.Submodel;
import org.eclipse.aas4j.v3.model.SubmodelElement;
import org.eclipse.aas4j.v3.model.impl.DefaultAssetAdministrationShellDescriptor;
import org.eclipse.aas4j.v3.model.impl.DefaultEndpoint;

import at.srfg.iasset.connector.component.config.ServiceProvider;
import at.srfg.iasset.repository.model.operation.OperationInvocation;
import at.srfg.iasset.repository.model.operation.OperationInvocationResult;
import at.srfg.iasset.repository.model.operation.OperationRequest;
import at.srfg.iasset.repository.model.operation.OperationRequestValue;
import at.srfg.iasset.repository.model.operation.OperationResult;
import at.srfg.iasset.repository.model.operation.OperationResultValue;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

@Dependent
public class RepositoryConnectionCDI implements RepositoryConnection {
	
	@Inject
	ServiceProvider serviceProvider;
	
	
	@Override
	public boolean register(AssetAdministrationShell theShell, URI uri) {
		// @formatter:off
		AssetAdministrationShellDescriptor descriptor = new DefaultAssetAdministrationShellDescriptor.Builder()
				.id(theShell.getId())
				.displayNames(theShell.getDisplayNames())
				.descriptions(theShell.getDescriptions())
				.endpoint(new DefaultEndpoint.Builder()
						.address(uri.toString())
						.type("shell")
						.build())
				.build();
		// @formatter:on
		//
		
		
		serviceProvider.getDirectoryInterface().register(theShell.getId(), descriptor);
		return true;
	}

	@Override
	public boolean unregister(String aasIdentifier) {
		// TODO Auto-generated method stub
		return false;
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
}
