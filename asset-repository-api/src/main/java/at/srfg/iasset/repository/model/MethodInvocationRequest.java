package at.srfg.iasset.repository.model;

public interface MethodInvocationRequest {
	
	<T> MethodInvocationRequest setParameter(T value);
	<T> MethodInvocationRequest setParameter(String idShort, T value);
	<T> T getParameter(Class<T> clazz);
	<T> T getParameter(String idShort, Class<T> clazz);
	MethodInvocationResult getResult();
//	MethodInvocationResult invoke(String aasIdentifier);
}
