package at.srfg.iasset.repository.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.ws.rs.ext.ContextResolver;

public class AASJacksonMapperProvider implements ContextResolver<ObjectMapper>{
	private ObjectMapper mapper;
	private SimpleAbstractTypeResolver typeResolver;
	@Override
	public ObjectMapper getContext(Class<?> type) {
		return mapper;
	}
	public AASJacksonMapperProvider() {
		initTypeResolver();
		buildMapper();
	}
	public ObjectMapper getMapper() {
		return mapper;
	}
	@SuppressWarnings("unchecked")
	private void initTypeResolver() {
        typeResolver = new SimpleAbstractTypeResolver();
        AASModelHelper.DEFAULT_IMPLEMENTATIONS
                .stream()
//                .filter(x -> !customDeserializers.containsKey(x.getInterfaceType()))
                .forEach(x -> typeResolver.addMapping(x.getInterfaceType(), x.getImplementationType()));

	}
	private void buildMapper() {
		mapper = JsonMapper.builder()
				.enable(SerializationFeature.INDENT_OUTPUT)
				.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
				.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
				.serializationInclusion(JsonInclude.Include.NON_EMPTY)
				.addModule(buildCustomSerializerModule())
				.addModule(buildImplementationModule())
				.addModule(new JavaTimeModule())
				//
				.annotationIntrospector(new AASModelIntrospector())
				.build();
		// add the mixin's to the object mapper
		AASModelHelper.JSON_MIXINS.entrySet().forEach(x -> mapper.addMixIn(x.getKey(), x.getValue()));

	}
    protected SimpleModule buildImplementationModule() {
        SimpleModule module = new SimpleModule();
        module.setAbstractTypes(typeResolver);
        return module;
    }

	private SimpleModule buildCustomSerializerModule() {
		SimpleModule module = new SimpleModule();
		return module;
	}
	
    public <T> void useImplementation(Class<T> aasInterface, Class<? extends T> implementation) {
        typeResolver.addMapping(aasInterface, implementation);
        buildMapper();
    }

}
