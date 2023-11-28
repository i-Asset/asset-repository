package at.srfg.iasset.repository.component;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.digitaltwin.aas4j.v3.model.BasicEventElement;
import org.eclipse.digitaltwin.aas4j.v3.model.DataElement;
import org.eclipse.digitaltwin.aas4j.v3.model.Operation;
import org.eclipse.digitaltwin.aas4j.v3.model.Property;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElement;

public interface ModelChangeProvider {
	public <T extends Property> void propertyCreated(String submodelIdentifier, String path, T dataElement);
	public <T extends Property> void propertyRemoved(String submodelIdentifier, String path, T dataElement);
	public <T extends BasicEventElement> void eventElementCreated(String submodelIdentifier, String path, T eventElement);
	public <T extends BasicEventElement> void eventElementRemoved(String submodelIdentifier, String path, T eventElement);
	public <T extends Operation> void operationCreated(String submodelIdentifier, String path, T dataElement);
	public <T extends Operation> void operationRemoved(String submodelIdentifier, String path, T dataElement);
	public <T extends SubmodelElement> void elementRemoved(String submodelIdentifier, String path, T element);
	/**
	 * Notify listeners that a {@link SubmodelElement} has been created
	 * @param <T>
	 * @param submodelIdentifier
	 * @param path
	 * @param element
	 */
	public <T extends SubmodelElement> void elementCreated(String submodelIdentifier, String path, T element);
	/**
	 * Notify listeners that the provided {@link DataElement}s value has been changed
	 * @param <T>
	 * @param submodelIdentifier
	 * @param path
	 * @param element
	 */
	public <T extends DataElement> void valueChanged(String submodelIdentifier, String path, T element);
	public void addListener(ModelListener listener);
	public void removeListener(ModelListener listener);
	static ModelChangeProvider getProvider() {
		return ChangeProvider.instance;
	}
	class ChangeProvider implements ModelChangeProvider {
		static ChangeProvider instance = new ChangeProvider();
		
		private final Set<ModelListener> listener = new HashSet<ModelListener>();
		

		@Override
		public <T extends Property> void propertyCreated(String submodelIdentifier, String path, T dataElement) {
			listener.forEach(t -> t.propertyCreated(submodelIdentifier, path, dataElement));
		}

		@Override
		public <T extends Property> void propertyRemoved(String submodelIdentifier, String path, T dataElement) {
			listener.forEach(t -> t.propertyRemoved(submodelIdentifier, path, dataElement));
			
		}

		@Override
		public <T extends BasicEventElement> void eventElementCreated(String submodelIdentifier, String path, T eventElement) {
			listener.forEach(t -> t.eventElementCreated(submodelIdentifier, path, eventElement));

		}


		@Override
		public <T extends BasicEventElement> void eventElementRemoved(String submodelIdentifier, String path, T eventElement) {
			listener.forEach(t -> t.eventElementRemoved(submodelIdentifier, path, eventElement));

			
		}

		@Override
		public <T extends Operation> void operationCreated(String submodelIdentifier, String path, T operation) {
			listener.forEach(t -> t.operationCreated(submodelIdentifier, path, operation));
			
		}

		@Override
		public <T extends Operation> void operationRemoved(String submodelIdentifier, String path, T operation) {
			listener.forEach(t -> t.operationRemoved(submodelIdentifier, path, operation));
			
		}

		@Override
		public void addListener(ModelListener listener) {
			this.listener.add(listener);
			
		}

		@Override
		public void removeListener(ModelListener listener) {
			this.listener.remove(listener);
			
		}


		@Override
		public <T extends SubmodelElement> void elementRemoved(String submodelIdentifier, String path, T element) {
			listener.forEach(x -> x.submodelElementRemoved(submodelIdentifier, path, element));

		
		}
		@Override
		public <T extends DataElement> void valueChanged(String submodelIdentifier, String path, T element) {
			// currently only data elements are subject to change events
			listener.forEach(x -> x.dataElementChanged(submodelIdentifier, path, element));
			
		}

		@Override
		public <T extends SubmodelElement> void elementCreated(String submodelIdentifier, String pathToElement, T element) {
			listener.forEach(x -> x.submodelElementCreated(submodelIdentifier, pathToElement, element));
			
		}
		
	}
}
