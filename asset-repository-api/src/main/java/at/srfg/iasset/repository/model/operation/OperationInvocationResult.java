package at.srfg.iasset.repository.model.operation;

import java.util.List;
import java.util.Optional;

import org.eclipse.digitaltwin.aas4j.v3.model.Operation;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElement;

public interface OperationInvocationResult {
	
	Operation getOperation();
	Object getResult();
	Object getResult(String idShort);
	<T> T getResult(Class<T> clazz);
	<T> T getResult(String idShort, Class<T> clazz);
	<T> List<T> getResultList(Class<T> clazz);
	<T> List<T> getResultList(String idShort, Class<T> clazz);
	Optional<SubmodelElement> getResultVariable(String idShort);

}
