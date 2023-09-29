package at.srfg.iasset.repository.component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.digitaltwin.aas4j.v3.model.DataElement;
import org.eclipse.digitaltwin.aas4j.v3.model.Identifiable;
import org.eclipse.digitaltwin.aas4j.v3.model.Operation;
import org.eclipse.digitaltwin.aas4j.v3.model.Referable;
import org.eclipse.digitaltwin.aas4j.v3.model.Reference;
import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElement;

/**
 * Model provider for a single IAsset Component.
 * 
 * The methods allow the manipulation of an {@link AssetAdministrationShell} 
 * 
 * @author dglachs
 *
 */
public interface IAssetModel {
	
	public AssetAdministrationShell getShell();
	

	/**
	 * Set 
	 * @param shell
	 */
	public boolean setAssetAdministrationShell(String identifier, AssetAdministrationShell shell);
	/**
	 * Add a new {@link Submodel} to the local environment
	 * @param aasIdentifier The identifier of the {@link AssetAdministrationShell}
	 * @param submodelIdentifier The identifier of the {@link Submodel}
	 * @param submodel
	 * @return The {@link Submodel} when successfully integrated in the environment, <code>null</code> otherwise
	 */
	public Submodel setSubmodel(String aasIdentifier, String submodelIdentifier, Submodel submodel);
	/**
	 * Obtain the {@link Submodel} 
	 * @param aasIdentifier
	 * @param submodelIdentifier
	 * @return
	 */
	public Submodel getSubmodel(String aasIdentifier, String submodelIdentifier);
	/**
	 * Retrieve a {@link SubmodelElement} by it's reference
	 * @param aasIdentifier
	 * @param reference
	 * @return
	 */
	public SubmodelElement getSubmodelElement(String aasIdentifier, Reference reference);

	
	public List<Reference> getSubmodels(String aasIdentifier);
	
	public Identifiable getRoot();

	/**
	 * Retrieve the full element (including sub-elements) 
	 * 
	 * @param reference
	 * @return
	 */
	public Optional<Referable> getElement(Reference reference);
	/**
	 * Retrieve the value of the referenced element. 
	 * 
	 * @param reference Reference pointing to a {@link DataElement}
	 * @return
	 */
	public Object getElementValue(String path);
	/**
	 * Retrieve the value of the referenced element. 
	 * 
	 * @param reference Reference pointing to a {@link DataElement}
	 * @return
	 */
	public Object getElementValue(Reference reference);
	/**
	 * Update the value of an existing element. 
	 * @param element The reference to the element whose value should be updated
	 * @param value The new value of the element
	 * @return
	 */
	public Referable setElementValue(Reference element, Object value);
	/**
	 * Update the value of an existing element. 
	 * @param element The path to the element whose value should be updated
	 * @param value The new value of the element
	 * @return
	 */
	public Referable setElementValue(String path, Object value);

	/**
	 * Create or replace an element
	 * @param parent Reference to the element containing the newly provided element
	 * @param element The element to add
	 * @return
	 */
	public Referable setElement(Reference parent, Referable element);
	/**
	 * Set the new value of the referenced element.
	 * @param path The relative path pointing to the element containing the new element - the path
	 *             must resolve to a  
	 * @param element The referable element including all containde children
	 * @return
	 */
	public Referable setElement(String path, Referable element);
	/**
	 * Convenience method to delete an object from the model. The {@link Referable} must 
	 * have proper parent element in order to resolve it by it's reference (see {@link Referable#asReference()})
	 * @param referable The element to remove from the model
	 * @return
	 */
	public boolean deleteElement(Referable referable);
	/**
	 * Execute the referenced operation
	 * @param reference Reference pointing to an {@link Operation}
	 * @param parameter Map holding the parameter values for each {@link Operation#getIn()}
	 * @return 
	 */
	public Map<String, Object> execute(Reference reference, Map<String, Object> parameter);
	public Map<String, Object> execute(String path, Map<String, Object> parameter);

	/**
	 * Retrieve an element by it's path
	 * @param path
	 * @return
	 */
	public Optional<Referable> getElement(String path);
	/**
	 * Retrieve an element by it's path with type check
	 * @param <T>
	 * @param path The path to the element
	 * @param clazz The expected type of the element, subclass of {@link Referable}
	 * @return
	 */

	<T extends Referable> Optional<T> getElement(String path, Class<T> clazz);

	/**
	 * Connect the asset model with a physical asset {@link Consumer} function in order 
	 * to accept a new value. The function is called when a new value for the property is provided.
	 * @param pathToProperty The path to the property 
	 * @param consumer The consumer function
	 */
	void setValueConsumer(String pathToProperty, Consumer<String> consumer);
	/**
	 * Connect the asset model with a physicial asset {@link Supplier} function in order to provide
	 * the actual value. The function is called whenever the property is serialized.
	 * @param pathToProperty The path to the property
	 * @param supplier The siupplier function
	 */
	void setValueSupplier(String pathToProperty, Supplier<String> supplier);
	/**
	 * Connect the asset model with a physical operation. This is realized with a 
	 * {@link Function} that takes a {@link Map} as arguments. The map represents
	 * the the (named) arguments for the operation to execute. 
	 * @param pathToOperation
	 * @param function The {@link Function} to execute
	 */
	void setFunction(String pathToOperation, Function<Map<String, Object>, Object> function);
//	/**
//	 * Add a model listener. The model listener methods are notified whenver
//	 * <ul>
//	 * <li>An EventElement is added/removed
//	 * <li>An Operation is added/removed
//	 * <li>A Property is added/removed
//	 * <li>A Property's value has been updated
//	 * </ul> 
//	 * @param listener
//	 */
//	void addModelListener(IAssetModelListener listener);
	void startEventProcessing();
	void stopEventProcessing();


	
}