package at.srfg.iasset.repository.model.helper;


import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElement;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.reflect.TypeToken;

import at.srfg.iasset.repository.component.RDFEnvironment;
import at.srfg.iasset.repository.component.ServiceEnvironment;
import at.srfg.iasset.repository.config.AASModelHelper;
import at.srfg.iasset.repository.model.helper.rdf.SubmodelElementValue;
import at.srfg.iasset.repository.model.helper.rdf.mapper.RDFMapper;
import at.srfg.iasset.repository.model.helper.value.exception.ValueMappingException;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
/**
 * Helper class, inspired from FAAST ...
 * 
 *
 */
public class RDFHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(RDFHelper.class);
	
	private static Map<Class<? extends SubmodelElement>, RDFMapper> mapper;
	
	static {
		
		if ( mapper == null) {
			mapper = new HashMap<Class<? extends SubmodelElement>, RDFMapper>();
			ScanResult scanResult = new ClassGraph()
					.enableAllInfo()
					.acceptPackages(RDFMapper.class.getPackageName())
					.scan();
			List<Class<?>> mapperClasses = scanResult.getClassesImplementing(RDFMapper.class).loadClasses();
			// build the mapper's
			for ( Class<?> clazz : mapperClasses) {
				Class<? extends RDFMapper> vmClass = (Class<? extends RDFMapper>) clazz;
				TypeToken typeVariable = TypeToken.of(vmClass).resolveType(RDFMapper.class.getTypeParameters()[0]);
				Class<? extends SubmodelElement> clazz2 = (Class<? extends SubmodelElement>) typeVariable.getRawType();
				try {
					RDFMapper valueMapper = ConstructorUtils.invokeConstructor(vmClass);
					mapper.put(clazz2, valueMapper);
				} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static <M extends SubmodelElement, V extends SubmodelElementValue> Model toRDF(RDFEnvironment environment, M submodelElement) throws ValueMappingException {
		Class<?> propertyInterface = AASModelHelper.getAasInterface(submodelElement.getClass());
		if ( mapper.containsKey(propertyInterface)) {
			return ((RDFMapper<M,V>)mapper.get(propertyInterface)).mapToRDF(environment, null, submodelElement);
		}
		return new TreeModel();
	}
	public static <M extends SubmodelElement, V extends SubmodelElementValue> Model toRDF(RDFEnvironment environment, Resource parent, M submodelElement) throws ValueMappingException {
		Class<?> propertyInterface = AASModelHelper.getAasInterface(submodelElement.getClass());
		if ( mapper.containsKey(propertyInterface)) {
			return ((RDFMapper<M,V>)mapper.get(propertyInterface)).mapToRDF(environment, parent, submodelElement);
			
		}
		return new TreeModel();
	}
	public static <M extends SubmodelElement, V extends SubmodelElementValue> M fromRDF(RDFEnvironment environment, Model model, M submodelElement) throws ValueMappingException {
		Class<?> propertyInterface = AASModelHelper.getAasInterface(submodelElement.getClass());
		if ( mapper.containsKey(propertyInterface)) {
			return ((RDFMapper<M,V>)mapper.get(propertyInterface)).mapToElement(environment, null, model, submodelElement);
		}
		return submodelElement;
	}
	public static <M extends SubmodelElement, V extends SubmodelElementValue> M fromRDF(RDFEnvironment environment, Resource parent, Model model, M submodelElement) throws ValueMappingException {
		Class<?> propertyInterface = AASModelHelper.getAasInterface(submodelElement.getClass());
		if ( mapper.containsKey(propertyInterface)) {
			return ((RDFMapper<M,V>)mapper.get(propertyInterface)).mapToElement(environment, parent, model, submodelElement);
		}
		return submodelElement;
	}
}
