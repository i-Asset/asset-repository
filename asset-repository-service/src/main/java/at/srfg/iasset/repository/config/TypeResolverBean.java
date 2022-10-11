package at.srfg.iasset.repository.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;

@Configuration
public class TypeResolverBean {

	@Bean
	@Primary
	public SimpleAbstractTypeResolver typeResolver() {
		SimpleAbstractTypeResolver typeResolver = new SimpleAbstractTypeResolver();
        AASModelHelper.DEFAULT_IMPLEMENTATIONS
	        .stream()
	        .forEach(x -> typeResolver.addMapping(x.getInterfaceType(), x.getImplementationType()));
        // add custom implementations here 
        // typeResolver.addMapping(null, null)
		return typeResolver;
	}
}
