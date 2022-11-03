package at.srfg.iasset.repository.component;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import org.eclipse.aas4j.v3.model.BasicEventElement;
import org.eclipse.aas4j.v3.model.DataElement;
import org.eclipse.aas4j.v3.model.Operation;
import org.eclipse.aas4j.v3.model.Property;
import org.eclipse.aas4j.v3.model.SubmodelElement;

import at.srfg.iasset.repository.model.helper.visitor.DataElementCollector;
import at.srfg.iasset.repository.model.helper.visitor.EventElementCollector;
import at.srfg.iasset.repository.model.helper.visitor.OperationCollector;
import at.srfg.iasset.repository.model.helper.visitor.PropertyCollector;

public interface ModelChangeProvider {
	public <T extends Property> void propertyAdded(T dataElement);
	public <T extends Property> void propertyChanged(T dataElement);
	public <T extends Property> void propertyRemoved(T dataElement);
	public <T extends BasicEventElement> void eventElementAdded(T eventElement);
	public <T extends BasicEventElement> void eventElementChanged(T eventElement);
	public <T extends BasicEventElement> void eventElementRemoved(T eventElement);
	public <T extends Operation> void operationAdded(T dataElement);
	public <T extends Operation> void operationChanged(T dataElement);
	public <T extends Operation> void operationRemoved(T dataElement);
	public <T extends SubmodelElement> void elementCreated(T element);
	public <T extends SubmodelElement> void elementRemoved(T element);
	public <T extends SubmodelElement> void elementChanged(T element);
	public void addListener(ModelListener listener);
	public void removeListener(ModelListener listener);
	static ModelChangeProvider getProvider() {
		return ChangeProvider.instance;
	}
	class ChangeProvider implements ModelChangeProvider {
		static ChangeProvider instance = new ChangeProvider();
		
		private final Set<ModelListener> listener = new HashSet<ModelListener>();
		

		@Override
		public <T extends Property> void propertyAdded(T dataElement) {
			listener.forEach(t -> t.propertyCreated(null, dataElement));
		}

		@Override
		public <T extends Property> void propertyChanged(T dataElement) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public <T extends Property> void propertyRemoved(T dataElement) {
			listener.forEach(t -> t.propertyRemoved(null, dataElement));
			
		}

		@Override
		public <T extends BasicEventElement> void eventElementAdded(T eventElement) {
			listener.forEach(t -> t.eventElementCreated(null, eventElement));

		}

		@Override
		public <T extends BasicEventElement> void eventElementChanged(T eventElement) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public <T extends BasicEventElement> void eventElementRemoved(T eventElement) {
			listener.forEach(t -> t.eventElementRemoved(null, eventElement));

			
		}

		@Override
		public <T extends Operation> void operationAdded(T operation) {
			listener.forEach(t -> t.operationCreated(null, operation));
			
		}

		@Override
		public <T extends Operation> void operationChanged(T dataElement) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public <T extends Operation> void operationRemoved(T operation) {
			listener.forEach(t -> t.operationRemoved(null, operation));
			
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
		public <T extends SubmodelElement> void elementCreated(T element) {
			new EventElementCollector().collect(element).forEach(new Consumer<BasicEventElement>() {

				@Override
				public void accept(BasicEventElement t) {
					listener.forEach(listener -> listener.eventElementCreated(null, t) );
					
				}
			});
			new PropertyCollector().collect(element).forEach(new Consumer<Property>() {

				@Override
				public void accept(Property t) {
					listener.forEach(listener -> listener.propertyCreated(null, t) );
					
				}
			});
			new OperationCollector().collect(element).forEach(new Consumer<Operation>() {

				@Override
				public void accept(Operation t) {
					listener.forEach(listener -> listener.operationCreated(null, t) );
					
				}
			});
			
		}

		@Override
		public <T extends SubmodelElement> void elementRemoved(T element) {
			new EventElementCollector().collect(element).forEach(new Consumer<BasicEventElement>() {

				@Override
				public void accept(BasicEventElement t) {
					listener.forEach(listener -> listener.eventElementRemoved(null, t) );
					
				}
			});
			new PropertyCollector().collect(element).forEach(new Consumer<Property>() {

				@Override
				public void accept(Property t) {
					listener.forEach(listener -> listener.propertyRemoved(null, t) );
					
				}
			});
			new OperationCollector().collect(element).forEach(new Consumer<Operation>() {

				@Override
				public void accept(Operation t) {
					listener.forEach(listener -> listener.operationRemoved(null, t) );
					
				}
			});
			
		}
		@Override
		public <T extends SubmodelElement> void elementChanged(T element) {
			// currently only prooperties are subject to change events
			new DataElementCollector().collect(element).forEach(new Consumer<DataElement>() {

				@Override
				public void accept(DataElement t) {
					listener.forEach(listener -> listener.dataElementChanged(null, t) );
					
				}
			});
			
		}
		
	}
}
