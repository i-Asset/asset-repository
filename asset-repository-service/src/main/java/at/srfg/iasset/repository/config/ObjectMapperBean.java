package at.srfg.iasset.repository.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.databind.module.SimpleModule;

@Configuration
public class ObjectMapperBean {
	@Autowired
	SimpleAbstractTypeResolver typeResolver;

	@Bean
	ObjectMapper objectMapper() {
		ObjectMapper mapper = JsonMapper.builder().enable(SerializationFeature.INDENT_OUTPUT)
				.enable(SerializationFeature.INDENT_OUTPUT)
				.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
				.serializationInclusion(JsonInclude.Include.NON_EMPTY)
				.build();
		return mapper;
	}
	@Bean(name = "aasMapper")
	ObjectMapper aasMapper() {
		ObjectMapper mapper = JsonMapper.builder()
				.enable(SerializationFeature.INDENT_OUTPUT)
				.enable(SerializationFeature.INDENT_OUTPUT)
				.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
				.serializationInclusion(JsonInclude.Include.NON_EMPTY)
				.addModule(buildCustomSerializerModule())
				.addModule(buildImplementationModule())
				//
				.annotationIntrospector(new AASModelIntrospector())
				.build();
		// add the mixin's to the object mapper
		AASModelHelper.JSON_MIXINS.entrySet().forEach(x -> mapper.addMixIn(x.getKey(), x.getValue()));
		// add the enumeration mixins
		
		return mapper;

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


}
