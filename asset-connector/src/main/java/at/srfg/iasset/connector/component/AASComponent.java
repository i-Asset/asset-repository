package at.srfg.iasset.connector.component;

import java.util.List;

import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.digitaltwin.aas4j.v3.model.Property;
import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElement;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.jboss.weld.inject.WeldInstance;
import org.slf4j.Logger;

import at.srfg.iasset.connector.api.ValueConsumer;
import at.srfg.iasset.connector.api.ValueSupplier;
import at.srfg.iasset.connector.environment.LocalEnvironment;
import at.srfg.iasset.messaging.EventHandler;
import at.srfg.iasset.messaging.EventProducer;
import at.srfg.iasset.messaging.exception.MessagingException;
import at.srfg.iasset.repository.component.ModelListener;
import at.srfg.iasset.repository.exception.ShellNotFoundException;
import at.srfg.iasset.repository.model.operation.OperationCallback;
import at.srfg.iasset.repository.model.operation.OperationInvocation;
import at.srfg.iasset.repository.model.operation.OperationInvocationResult;
import at.srfg.iasset.repository.model.operation.exception.OperationInvocationException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
/**
 * The {@link AASComponent} represents the active I4.0 component. As such it
 * <ul>
 * <li>provides the HTTP endpoint for the outer world, the HTTP endpoint serves the AAS model
 * <li>manages the messaging functionality driven by the AAS model
 * <li>maintains the AAS model (in memory)
 * </ul>
 * <p>
 * For the required interaction with the surrounding application, the {@link AASComponent}
 * provides the corresponding methods:
 * <ul>
 * <li>{@link #add(AssetAdministrationShell)}: Registers an AAS with the component
 * <li>{@link #add(String, Submodel)}: Registers a Submodle with the component, and assigns it to the provided AAS
 * <li>{@link #registerCallback(String, EventHandler, String...)}: Register an {@link EventHandler} to react on incoming events
 * <li>{@link #registerCallback(String, String, String, OperationCallback)}: Register an {@link OperationCallback} for method execution 
 * <li>{@link #registerCallback(String, String, String, ValueConsumer)}: Register an {@link ValueConsumer} for accepting incoming values
 * <li>{@link #registerCallback(String, String, String, ValueSupplier)}: Register an {@link ValueSupplier} providing values from the physical asset
 * <li>{@link #getElementValue(String, String, String, Class)}: Allows type-safe access to AAS model elements
 * <li>{@link #setElementValue(String, String, String, Object)}: Allows type-safe modification of AAS model elements
 * </ul>
 */
@ApplicationScoped
public class AASComponent {
	/**
	 * The container managing the dependency injection
	 */
	private static WeldContainer weldContainer;
	
	
	@Inject
	private LocalEnvironment environment;
	
	@Inject
	private Logger logger;
	
	@Inject
	Instance<AASComponentModel> model;
	
	/**
	 * Add an existing {@link AssetAdministrationShell} to the current component
	 * @param shell
	 */
	public void add(AssetAdministrationShell shell) {
		environment.addAdministrationShell(shell);
	}
	/**
	 * Add an existing {@link Submodel} to an existing {@link AssetAdministrationShell} 
	 * @param aasIdentifier The identifier of the existing {@link AssetAdministrationShell}
	 * @param submodel The {@link Submodel} to add
	 * @throws ShellNotFoundException 
	 */
	public void add(String aasIdentifier, Submodel submodel) throws ShellNotFoundException {
		environment.addSubmodel(aasIdentifier, submodel);
	}
	public void alias(String aasIdentifier, String alias) {
		environment.addHandler(aasIdentifier, alias);
	}
	public void addListener(ModelListener modelListener) {
		environment.addModelListener(modelListener);
	}
	public void removeListener(ModelListener modelListener) {
		environment.removeModelListener(modelListener);
	}
	public boolean loadPattern(String patternId) {
		return environment.loadIntegrationPattern(patternId);		
	}
	public boolean loadPattern(Submodel patternSubmodel) {
		return environment.loadIntegrationPattern(patternSubmodel);
	}
	/**
	 * Start the REST component
	 */
	public void startEndpoint() {
		environment.startEndpoint();
	}
	public void stopEndpoint() {
		environment.shutdownEndpoint();
	}
	public ConnectorEndpoint getEndpoint() {
		return environment.getEndpoint();
	}
	
	@PostConstruct
	protected void postConstruct() {
		initializeComponent();
	}
	@PreDestroy
	protected void preDestroy() {
		environment.shutdownEndpoint();
	}
	/**
	 * Method required in order to get the component up and running
	 * @param init
	 * @throws Exception
	 */
	public void init(@Observes @Priority(-100) @Initialized(ApplicationScoped.class) Object init) throws Exception {
		
	}
	private void initializeComponent() {
		// do the following:
		if (! model.isUnsatisfied()) {
//			// load provided AAS data to the LocalEnvironment
			model.get().loadData(environment);
			// inject business logic
			model.get().injectLogic(environment);
		}
		// - loadData
		startEndpoint();
		// - startEndpoint
		// - register AAS'es with Directory-Service!
	}

	public static AASComponent create() {
		Weld weld = new Weld();
		weldContainer = weld.initialize();
		WeldInstance<AASComponent> instance = weldContainer.select(AASComponent.class);
		return instance.get();

	}
	/**
	 * Stop the AASComponent's container functionality. 
	 * <p>This will stop the service endpoint and close all event 
	 * subscribers and producers.
	 * </p>
	 * 
	 */
	public static void close() {
		if (weldContainer!=null && weldContainer.isRunning()) {
			weldContainer.close();
		}
	}
	/**
	 * Register a type safe {@link ValueConsumer} function for {@link Property} element. The callback function is 
	 * invoked whenever a new value is provided for the property via REST services and may accept new values
	 * from the AAS world for the physical component
	 * @param <T>
	 * @param aasIdentifier The id of the {@link AssetAdministrationShell}
	 * @param submodelIdentifier The id of the {@link Submodel}, must be part of the AAS!
	 * @param path The dot-separated path of the {@link Property} 
	 * @param valueCallback The {@link ValueConsumer} function implementing the {@link ValueConsumer#accept(Object)} method!
	 */
	public <T> void registerCallback(String aasIdentifier, String submodelIdentifier, String path, ValueConsumer<T> valueCallback) {
		environment.registerValueCallback(aasIdentifier, submodelIdentifier, path, valueCallback);
		
	}
	/**
	 * Register a type safe {@link ValueSupplier} function for {@link Property} element. The callback function is 
	 * invoked whenever a new value is provided for the property via REST services and is intended to 
	 * provide the physical component's value to the AAS world.
	 * @param <T>
	 * @param aasIdentifier The id of the {@link AssetAdministrationShell}
	 * @param submodelIdentifier The id of the {@link Submodel}, must be part of the AAS!
	 * @param path The dot-separated path of the {@link Property} 
	 * @param valueSupplier The {@link ValueSupplier} function implementing the {@link ValueSupplier#get()} method!
	 */
	public <T> void registerCallback(String aasIdentifier, String submodelIdentifier, String path, ValueSupplier<T> valueCallback) {
		environment.registerValueCallback(aasIdentifier, submodelIdentifier, path, valueCallback);
		
	}
	public void registerCallback(String aasIdentifier, String submodelIdentifier, String path, OperationCallback function) {
		environment.registerOperation(aasIdentifier, submodelIdentifier, path, function);
		
	}
	public <T> void registerCallback(String semanticId, EventHandler<T> eventHandler, String ... globalReferences) throws MessagingException {
		environment.registerEventHandler(eventHandler, semanticId, globalReferences);
	}
	public <T> EventProducer<T> getEventProducer(String semanticId, Class<T> clazz) {
		return environment.getEventProducer(semanticId, clazz);
	}
	/**
	 * Obtain a method based on a semantic identifier 
	 * @param semanticId
	 * @return
	 * @throws OperationInvocationException 
	 */
	public OperationInvocation getOperationRequest(String semanticId) throws OperationInvocationException {
		// search environment for operation with semantic id
		return environment.getOperationInvocation(semanticId);
		
	}
	public <R, I> R getOperationResult(String semanticId, I parameter, Class<R> clazz) throws OperationInvocationException {
		OperationInvocation invocation = environment.getOperationInvocation(semanticId);
		OperationInvocationResult result = invocation.invoke();
		return result.getResult(clazz);
	}
	public <R, I> List<R> getOperationResultList(String semanticId, I parameter, Class<R> clazz) throws OperationInvocationException {
		OperationInvocation invocation = environment.getOperationInvocation(semanticId);
		OperationInvocationResult result = invocation.invoke();
		return result.getResultList(clazz);
	}
	/**
	 * Allows type-safe modification of the AAS model element identified by aas-id, submodel-id and path.
	 * 
	 * @param <T> The object class corresponding to the structure of the {@link SubmodelElement}
	 * @param aasIdentifier The AAS identifier
	 * @param submodelIdentifier The Submodel identifier
	 * @param path The dot separated idShort path to the {@link SubmodelElement}
	 * @param value The object of type &lt;T&gt;
	 */
	public <T> void setElementValue(String aasIdentifier, String submodelIdentifier, String path, T value) {
		environment.setElementValue(aasIdentifier, submodelIdentifier, path, value);
	}
	/**
	 * Type-safe value access. The respective AAS model element is identified by aas-id, submodel-id and path.
	 * 
	 * @param <T> The object class corresponding to the structure of the {@link SubmodelElement}
	 * @param aasIdentifier The AAS identifier
	 * @param submodelIdentifier The Submodel identifier
	 * @param path The dot separated idShort path to the {@link SubmodelElement}
	 * @param value The collected values in the object of type &lt;T&gt;
	 */
	public <T> T getElementValue(String aasIdentifier, String submodelIdentifier, String path, Class<T> clazz) {
		return environment.getElementValue(aasIdentifier, submodelIdentifier, path, clazz);
		
	}
	public void info(String message, Object ...parameters) {
		logger.info(message, parameters);
	}
	/**
	 * Regis
	 * @param aasIdentifier
	 */
	public void register(String aasIdentifier) {
		
		environment.registerAssetAdministrationShell(aasIdentifier);
		// add the current component 
	}
}
