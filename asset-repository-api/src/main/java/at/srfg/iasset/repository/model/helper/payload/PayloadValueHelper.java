package at.srfg.iasset.repository.model.helper.payload;


import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.reflect.TypeToken;

import at.srfg.iasset.repository.config.AASModelHelper;
import at.srfg.iasset.repository.model.helper.value.mapper.ValueMapper;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
/**
 * Helper class, inspired from FAAST ...
 * 
 *
 */
public class PayloadValueHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(PayloadValueHelper.class);
	
	private static Map<Class<?>, PayloadValueMapper> mapper;
	
	static {
		
		if ( mapper == null) {
			mapper = new HashMap<Class<?>, PayloadValueMapper>();
			ScanResult scanResult = new ClassGraph()
					.enableAllInfo()
					.acceptPackages(ValueMapper.class.getPackageName())
					.scan();
			List<Class<?>> mapperClasses = scanResult.getClassesImplementing(PayloadValueMapper.class).loadClasses();
			// build the mapper's
			for ( Class<?> clazz : mapperClasses) {
				Class<? extends PayloadValueMapper> vmClass = (Class<? extends PayloadValueMapper>) clazz;
				TypeToken typeVariable = TypeToken.of(vmClass).resolveType(ValueMapper.class.getTypeParameters()[0]);
				Class<?> clazz2 = (Class<?>) typeVariable.getRawType();
				try {
					PayloadValueMapper valueMapper = ConstructorUtils.invokeConstructor(vmClass);
					mapper.put(clazz2, valueMapper);
				} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
					e.printStackTrace();
				}
			}
		}
	}
	@SuppressWarnings("unchecked")
	public static <M, V extends PayloadValue> V toValue(M submodelElement) {
		Class<?> propertyInterface = AASModelHelper.getAasInterface(submodelElement.getClass());
		if ( mapper.containsKey(propertyInterface)) {
			return (V) ((PayloadValueMapper<M,V>)mapper.get(propertyInterface)).mapToValue(submodelElement);
		}
		return null;
		
	}
	public static <M, V extends PayloadValue> M fromValue(V value, Class<M> modelElement) {
		if (value != null) {
			Class<?> propertyInterface = AASModelHelper.getAasInterface(modelElement);
			if ( mapper.containsKey(propertyInterface)) {
				return (M) ((PayloadValueMapper<M,V>)mapper.get(propertyInterface)).mapFromValue(value);
			}
		}
		return null;
	}
}
