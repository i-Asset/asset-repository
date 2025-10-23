package at.srfg.iasset.connector.component.endpoint.rest;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShellDescriptor;
import org.eclipse.digitaltwin.aas4j.v3.model.ConceptDescription;
import org.eclipse.digitaltwin.aas4j.v3.model.Referable;
import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElement;

import at.srfg.iasset.connector.component.endpoint.RepositoryConnection;
import at.srfg.iasset.repository.api.DirectoryInterface;
import at.srfg.iasset.repository.api.IAssetAdministrationShellRepositoryInterface;
import at.srfg.iasset.repository.api.SubmodelRepositoryInterface;
import at.srfg.iasset.repository.connectivity.ConnectionProvider;
import at.srfg.iasset.repository.model.operation.OperationRequest;
import at.srfg.iasset.repository.model.operation.OperationRequestValue;
import at.srfg.iasset.repository.model.operation.OperationResult;
import at.srfg.iasset.repository.model.operation.OperationResultValue;
import jakarta.ws.rs.ServiceUnavailableException;

public class RepositoryRestConnector implements RepositoryConnection {

	private final ConnectionProvider connection;

	public RepositoryRestConnector(URI repositoryURI) {
		this.connection = ConnectionProvider.getConnection(repositoryURI);
	}

	private IAssetAdministrationShellRepositoryInterface getRepositoryService() {
		return connection.getRepositoryInterface();
	}

	private DirectoryInterface getDirectoryService() {
		return connection.getIAssetDirectory();
	}
	private SubmodelRepositoryInterface getSubmodelRepositoryInterface() {
		return connection.getSubmodelInterface();
	}

	@Override
	public AssetAdministrationShell save(AssetAdministrationShell shell) {
		try {
			return getRepositoryService().setAssetAdministrationShell(shell.getId(), shell);
		} catch (ServiceUnavailableException e) {
			return null;
		}

	}

	@Override
	public Optional<AssetAdministrationShell> getAssetAdministrationShell(String aasIdentifier) {
		AssetAdministrationShell theShell  =getRepositoryService().getAssetAdministrationShell(aasIdentifier);
		return Optional.ofNullable(theShell);
	}

	@Override
	public Optional<Submodel> getSubmodel(String aasIdentifier, String submodelIdentifier) {
		Submodel theSubmodel = getRepositoryService().getSubmodel(aasIdentifier, submodelIdentifier);
		return Optional.ofNullable(theSubmodel);
	}

	@Override
	public Optional<Submodel> getSubmodel(String submodelIdentifier) {
		Submodel sub = getSubmodelRepositoryInterface().getSubmodel(submodelIdentifier);
		return Optional.ofNullable(sub);
	}

	@Override
	public <T extends SubmodelElement> Optional<T> getSubmodelElement(String aasIdentifier, String submodelIdentifier,
			String path, Class<T> elementClass) {
		try {
			Referable result = getRepositoryService().getSubmodelElement(aasIdentifier, submodelIdentifier, path);
			if (result != null && elementClass.isInstance(result)) {
				return Optional.of(elementClass.cast(result));
			}
			return Optional.empty();
				
		} catch (ServiceUnavailableException e) {
			// Improve Exception Handling
			return null;
		}
	}

	@Override
	public Optional<ConceptDescription> getConceptDescription(String conceptIdentifier) {
		ConceptDescription cd = getRepositoryService().getConceptDescription(conceptIdentifier);

		return Optional.ofNullable(cd);
	}

	@Override
	public boolean register(AssetAdministrationShellDescriptor descriptor) {
		//
		try {
			getDirectoryService().register(descriptor.getId(), descriptor);
			return true;
		} catch (ServiceUnavailableException e) {
			return false;
		}

	}

	@Override
	public boolean unregister(String aasIdentifier) {
		try {
			getDirectoryService().unregister(aasIdentifier);
			return true;
		} catch (ServiceUnavailableException e) {
			return false;
		}

	}

	@Override
	public OperationResult invokeOperation(String aasIdentifier, String submodelIdentifier, String path, OperationRequest parameter) {
		try {
			return getRepositoryService().invokeOperation(aasIdentifier, submodelIdentifier, path, parameter);
		} catch (ServiceUnavailableException e) {
			return null;
		}
		
	}

	@Override
	public OperationResultValue invokeOperation(String aasIdentifier, String submodelIdentifier, String path,
			OperationRequestValue parameter) {
		try {
			return getRepositoryService().invokeOperation(aasIdentifier, submodelIdentifier, path, parameter);
		} catch (ServiceUnavailableException e) {
			return null;
		}
	}

	@Override
	public Optional<AssetAdministrationShellDescriptor> findImplementation(String semanticId, String ... additional) {
		return Optional.ofNullable( getDirectoryService().lookupBySemanticIds(semanticId, List.of(additional)));
	}

}
