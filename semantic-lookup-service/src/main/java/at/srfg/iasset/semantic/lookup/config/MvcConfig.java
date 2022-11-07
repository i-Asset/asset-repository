package at.srfg.iasset.semantic.lookup.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {

	@Value("${nimble.corsEnabled}")
	private String corsEnabled;

	private static final Logger logger = LoggerFactory.getLogger(MvcConfig.class);

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html")
				.addResourceLocations("classpath:/META-INF/resources/");

		registry.addResourceHandler("/webjars/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		if (corsEnabled.equals("true")) {
			logger.info("Enabling CORS");
			registry.addMapping("/**").allowedOrigins("*")
					.allowedHeaders("*")
					.exposedHeaders("Authorization")
					.allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH","OPTIONS");
		}
	}

}


