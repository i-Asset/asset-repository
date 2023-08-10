package at.srfg.iasset.repository.model;

public interface MethodInvocationResult {
	<T> T getValue(String idShort, Class<T> clazz);
}
