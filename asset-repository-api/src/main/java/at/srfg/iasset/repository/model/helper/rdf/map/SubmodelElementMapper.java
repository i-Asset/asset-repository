package at.srfg.iasset.repository.model.helper.rdf.map;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElement;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Namespace;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.reflect.TypeToken;

import at.srfg.iasset.repository.component.RDFEnvironment;
import at.srfg.iasset.repository.config.AASModelHelper;
import at.srfg.iasset.repository.model.helper.RDFHelper;
import at.srfg.iasset.repository.model.helper.value.exception.ValueMappingException;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;

public abstract class SubmodelElementMapper<M extends SubmodelElement> {
	private static final Logger LOGGER = LoggerFactory.getLogger(RDFHelper.class);
	
	private static Map<Class<? extends SubmodelElement>, SubmodelElementMapper> mapper;
	
	static {
		
		if ( mapper == null) {
			mapper = new HashMap<Class<? extends SubmodelElement>, SubmodelElementMapper>();
			ScanResult scanResult = new ClassGraph()
					.enableAllInfo()
					.acceptPackages(SubmodelElementMapper.class.getPackageName())
					.scan();
			// check for subclasses
			List<Class<?>> mapperClasses = scanResult.getSubclasses(SubmodelElementMapper.class).loadClasses();
			// build the mapper's
			for ( Class<?> clazz : mapperClasses) {
				Class<? extends SubmodelElementMapper> vmClass = (Class<? extends SubmodelElementMapper>) clazz;
				TypeToken typeVariable = TypeToken.of(vmClass).resolveType(SubmodelElementMapper.class.getTypeParameters()[0]);
				Class<? extends SubmodelElement> clazz2 = (Class<? extends SubmodelElement>) typeVariable.getRawType();
				try {
					SubmodelElementMapper valueMapper = ConstructorUtils.invokeConstructor(vmClass);
					mapper.put(clazz2, valueMapper);
				} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static <M extends SubmodelElement> SubmodelElementMapper<M> getMapper(M submodelElement) throws ValueMappingException {
		Class<?> propertyInterface = AASModelHelper.getAasInterface(submodelElement.getClass());
		if ( mapper.containsKey(propertyInterface)) {
			return ((SubmodelElementMapper<M>)mapper.get(propertyInterface));
		}
		throw new ValueMappingException(String.format("Mapper for interface (%s) not found!", propertyInterface.getName()));
	}
	public final Model toRDF(Resource parent, M modelElement, Model model, RDFEnvironment rdfEnvironment) {
        Resource subject = (parent == null ? SimpleValueFactory.getInstance().createBNode() : parent);
        rdfEnvironment.getSemanticIdentifier(modelElement).ifPresent((predicate)-> {
        	// manage the namespace
        	addNameSpace(model, predicate);
        	// 
        	addToModel(modelElement,subject, model, rdfEnvironment);
        });
		return model;
	}
	protected abstract Optional<Value> addToModel(M modelElement, Resource subject, Model model, RDFEnvironment rdfEnvironment);
	// 
	protected void addNameSpace(Model model, IRI namespace) {
		addNameSpace(model, namespace.getNamespace());
	}
	protected void addNameSpace(Model model, String namespace) {
		Optional<Namespace> vocab = model.getNamespace("");
		if ( vocab.isEmpty()) {
			model.setNamespace("", namespace);
		}
		else {
			if (! vocab.get().getName().equals(namespace)) {
				// check for the given namespace in the model
				Optional<Namespace> ns = model.getNamespaces().stream()
						.filter((Namespace t)-> t.getName().equals(namespace))
						.findFirst();
				// namespace is not present -> add
				if ( ns.isEmpty()) {
					long nsCount = model.getNamespaces().stream().filter((Namespace t) -> !t.getPrefix().startsWith("ns")).count();
					model.setNamespace(String.format("ns%s", nsCount), namespace);
				}
			}
		}
	}
	private Resource checkRoot(Resource resource, Model model) {
		if ( resource == null ) {
			
		}
		return resource;
	}


}
