package at.srfg.iasset.repository.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.AbstractTypeResolver;
import com.fasterxml.jackson.databind.ObjectMapper;

@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Autowired
	ObjectMapper aasMapper;
	@Autowired
	AbstractTypeResolver typeResolver;

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		WebMvcConfigurer.super.configureMessageConverters(converters);
		// be sure to add the StringHttpMessageConverter as the very first ...
		// otherwise SWAGGER UI cannot be rendered!
		converters.add(new StringHttpMessageConverter());
		// add JSON Message Converter
		converters.add(createXmlJsonpMessageConverter());
	}

	@Bean
	public HttpMessageConverter<Object> createXmlJsonpMessageConverter() {
		return new MappingJackson2HttpMessageConverter(aasMapper);

	}

}