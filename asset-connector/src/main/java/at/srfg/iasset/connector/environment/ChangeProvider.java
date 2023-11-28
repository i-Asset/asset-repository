package at.srfg.iasset.connector.environment;

import org.eclipse.digitaltwin.aas4j.v3.model.Referable;
import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElement;

import at.srfg.iasset.repository.component.ModelListener;

public interface ChangeProvider {
	/**
	 * Register a new {@link ModelListener} to be informed on model changes
	 * @param listener
	 */
	void addModelListener(ModelListener listener);
	/**
	 * Remove the provided {@link ModelListener} from the listener's list
	 * @param listener
	 */
	void removeModelListener(ModelListener listener);
	/**
	 * Notify all listeners, that a {@link SubmodelElement} has been changed
	 * @param submodel The {@link Submodel} containing the {@link SubmodelElement}
	 * @param path The path to the {@link SubmodelElement}
	 * @param submodelElement The changed {@link SubmodelElement}
	 */
	void notifyChange(Submodel submodel, String path, SubmodelElement submodelElement);
	/**
	 * Notify all listeners, that a {@link SubmodelElement} has been deleted from the environment
	 * @param submodel The {@link Submodel} containing the {@link SubmodelElement}
	 * @param path The path to the {@link SubmodelElement}
	 * @param submodelElement The removed {@link SubmodelElement}
	 */
	<T extends Referable> void notifyDeletion(Submodel submodel, String pathToElement, T deletedElement);
	/**
	 * Notify all listeners, that a {@link SubmodelElement} has been added to the environment
	 * @param submodel The {@link Submodel} containing the {@link SubmodelElement}
	 * @param path The path to the {@link SubmodelElement}
	 * @param submodelElement The newly added {@link SubmodelElement}
	 */
	<T extends Referable> void notifyCreation(final Submodel submodel, String pathToElement, T createdElement);
}
