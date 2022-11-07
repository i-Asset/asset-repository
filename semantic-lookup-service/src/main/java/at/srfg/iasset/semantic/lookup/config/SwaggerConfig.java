//package at.srfg.iasset.semantic.lookup.config;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//@Configuration
//@EnableSwagger2
//@EnableWebMvc
//public class SwaggerConfig {
//
//	private static final Logger logger = LoggerFactory.getLogger(SwaggerConfig.class);
//
//	@Value("${nimble.platformHost:}")
//	private String platformHost;
//
//	public static final ApiInfo DEFAULT_API_INFO = new ApiInfoBuilder()
//			.title("Common Data Dictionary Service API")
//			.description(
//					"API documentation for Common Data Dictionary Service's APIs.")
//			.version("V1.0.0")
//			.build();
//
//	@Bean
//	public Docket api() {
//		platformHost = platformHost.replace("https://", "");
//		platformHost = platformHost.replace("http://","");
//
//		return new Docket(DocumentationType.SWAGGER_2)
//				.host(platformHost)
//				.select()
//				.apis(RequestHandlerSelectors.basePackage("at.srfg.iot"))
//				.paths(PathSelectors.any())
//				.build()
//				.apiInfo(DEFAULT_API_INFO);
//	}
//
//}
