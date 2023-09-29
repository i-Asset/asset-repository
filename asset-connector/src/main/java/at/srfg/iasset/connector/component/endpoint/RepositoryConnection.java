package at.srfg.iasset.connector.component.endpoint;

import java.net.URI;
import java.util.Optional;

import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.digitaltwin.aas4j.v3.model.ConceptDescription;
import org.eclipse.digitaltwin.aas4j.v3.model.Operation;
import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElement;

import at.srfg.iasset.connector.component.endpoint.rest.RepositoryRestConnector;
import at.srfg.iasset.repository.model.operation.OperationRequest;
import at.srfg.iasset.repository.model.operation.OperationRequestValue;
import at.srfg.iasset.repository.model.operation.OperationResult;
import at.srfg.iasset.repository.model.operation.OperationResultValue;

public interface RepositoryConnection {
	/**
	 * register the provided {@link AssetAdministrationShell} with the Asset Directory.
	 * @param shellToRegister The shell to register
	 * @param uri The service endpoint for this particular {@link AssetAdministrationShell}
	 * @return <code>true</code> when successfully registered, <code>false</code> otherwise
	 */
	boolean register(AssetAdministrationShell shellToRegister, URI uri);
	/**
	 * Unregister the {@link AssetAdministrationShell} from the Asset Directory.
	 * @param aasIdentifier
	 * @return <code>true</code> when successfully unregistered, <code>false</code> otherwise
	 */
	boolean unregister(String aasIdentifier);
	/**
	 * Store the provided {@link AssetAdministrationShell} with the Asset Repository
	 * @param shell
	 */
	AssetAdministrationShell save(AssetAdministrationShell shell);
	/**
	 * Obtain the {@link AssetAdministrationShell} based on it's identifier
	 * @param aasIdentifier the unique identifier of the {@link AssetAdministrationShell}
	 * @return The aas object or null when not found
	 */
	Optional<AssetAdministrationShell> getAssetAdministrationShell(String aasIdentifier);
	/**
	 * Obtain a {@link Submodel} based on it's identifier. The submodel must be assigned to the {@link AssetAdministrationShell}
	 * @param aasIdentifier
	 * @param submodelIdentifier
	 * @return
	 */
	Optional<Submodel> getSubmodel(String aasIdentifier, String submodelIdentifier);
	Optional<Submodel> getSubmodel(String submodelIdentifier);
	Optional<ConceptDescription> getConceptDescription(String conceptIdentifier);
	
	
	<T extends SubmodelElement> Optional<T> getSubmodelElement(String aasIdentifier, String submodelIdentifier, String path, Class<T> elementClass);
	/**
	 * Invoke an {@link Operation}
	 * @param aasIdentifier The (active) {@link AssetAdministrationShell} referencing the {@link Submodel}
	 * @param submodelIdentifier The {@link Submodel} 
	 * @param path The path pointing to the Operation
	 * @param parameter The Operations parameters
	 * @return The AAS modelled result of the invocation.
	 */
	OperationResult invokeOperation(String aasIdentifier, String submodelIdentifier, String path, OperationRequest parameter);
	/**
	 * Invoke an {@link Operation}
	 * @param aasIdentifier The (active) {@link AssetAdministrationShell} referencing the {@link Submodel}
	 * @param submodelIdentifier The {@link Submodel} 
	 * @param path The path pointing to the Operation
	 * @param parameter The Operations parameters
	 * @return The result of the invocation.
	 */
	OperationResultValue invokeOperation(String aasIdentifier, String submodelIdentifier, String path, OperationRequestValue parameter);
	/**
	 * Obtain a connection to the repository service. The required URI denotes the base URI. The {@link RepositoryConnection}
	 * acts as s shortcut to
	 * <ul>
	 * <li><code>IAssetDirectoryService</code>
	 * <li>IAssetAdministrationShellRepositoryInterface
	 * </ul>
	 * @param uri The base URI of the repository service. 
	 * @return 
	 */
	static RepositoryConnection getConnector(URI uri) {
		return new RepositoryRestConnector(uri);
	}
}
