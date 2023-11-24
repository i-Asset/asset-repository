package at.srfg.iasset.repository.model.helper;


import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.reflect.TypeToken;

import at.srfg.iasset.repository.component.ServiceEnvironment;
import at.srfg.iasset.repository.config.AASModelHelper;
import at.srfg.iasset.repository.model.helper.value.SubmodelElementValue;
import at.srfg.iasset.repository.model.helper.value.mapper.ValueMapper;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
/**
 * Helper class, inspired from FAAST ...
 * 
 *
 */
public class ValueHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(ValueHelper.class);
	
	private static Map<Class<? extends SubmodelElement>, ValueMapper> mapper;
	
	static {
		
		if ( mapper == null) {
			mapper = new HashMap<Class<? extends SubmodelElement>, ValueMapper>();
			ScanResult scanResult = new ClassGraph()
					.enableAllInfo()
					.acceptPackages(ValueMapper.class.getPackageName())
					.scan();
			List<Class<?>> mapperClasses = scanResult.getClassesImplementing(ValueMapper.class).loadClasses();
			// build the mapper's
			for ( Class<?> clazz : mapperClasses) {
				Class<? extends ValueMapper> vmClass = (Class<? extends ValueMapper>) clazz;
				TypeToken typeVariable = TypeToken.of(vmClass).resolveType(ValueMapper.class.getTypeParameters()[0]);
				Class<? extends SubmodelElement> clazz2 = (Class<? extends SubmodelElement>) typeVariable.getRawType();
				try {
					ValueMapper valueMapper = ConstructorUtils.invokeConstructor(vmClass);
					mapper.put(clazz2, valueMapper);
				} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
					e.printStackTrace();
				}
			}
		}
	}
	@SuppressWarnings("unchecked")
	public static <M extends SubmodelElement, V extends SubmodelElementValue> V toValue(M submodelElement) {
		Class<?> propertyInterface = AASModelHelper.getAasInterface(submodelElement.getClass());
		if ( mapper.containsKey(propertyInterface)) {
			return (V) ((ValueMapper<M,V>)mapper.get(propertyInterface)).mapToValue(submodelElement);
		}
		return null;
		
	}
	public static <M extends SubmodelElement, V extends SubmodelElementValue> M applyValue(M modelElement, V value) {
		return modelElement;
	}
	@SuppressWarnings("unchecked")
	public static <M extends SubmodelElement, V extends SubmodelElementValue> M applyValue(M modelElement, JsonNode node) {
		Class<?> propertyInterface = AASModelHelper.getAasInterface(modelElement.getClass());
		if ( mapper.containsKey(propertyInterface)) {
			return (M) ((ValueMapper<M,V>)mapper.get(propertyInterface)).mapValueToElement(modelElement, node);
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	public static <M extends SubmodelElement, V extends SubmodelElementValue> M applyValue(ServiceEnvironment environment, M modelElement, JsonNode node) {
		Class<?> propertyInterface = AASModelHelper.getAasInterface(modelElement.getClass());
		if ( mapper.containsKey(propertyInterface)) {
			return (M) ((ValueMapper<M,V>)mapper.get(propertyInterface)).mapValueToTemplate(environment, modelElement, node);
		}
		return null;
	}
	public static <M extends SubmodelElement, V extends SubmodelElementValue> M applyValue(ServiceEnvironment environment, M instanceElement, M templateElement, JsonNode node) {
		Class<?> propertyInterface = AASModelHelper.getAasInterface(templateElement.getClass());
		
		if ( mapper.containsKey(propertyInterface)) {
			return (M) ((ValueMapper<M,V>)mapper.get(propertyInterface)).mapValueToTemplate(environment, instanceElement, templateElement, node);
		}
		return null;
	}

}
