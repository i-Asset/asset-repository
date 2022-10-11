package at.srfg.iasset.connector.environment;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface LocalEnvironment {
	
	public void addModelListener(ModelListener listener);
	public void removeModelListener(ModelListener listener);
	
	public void setValueConsumer(String aasIdentifier, String submodelIdentifier, String path, Consumer<?> consumer);
	public void setValueSupplier(String aasIdentifier, String submodelIdentifier, String path, Supplier<?> consumer);
	
	public void setOperationFunction(String aasIdentifier, String submodelIdentifier, String path, Function<?,?> consumer);

}
