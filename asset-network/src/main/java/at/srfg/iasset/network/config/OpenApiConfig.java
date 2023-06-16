package at.srfg.iasset.network.config;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

  @Value("${iasset.openapi.dev-url}")
  private String devUrl;

  @Value("${iasset.openapi.prod-url}")
  private String prodUrl;

  @Bean
  public OpenAPI myOpenAPI() {
    Server devServer = new Server();
    devServer.setUrl(devUrl);
    devServer.setDescription("Server URL in Development environment");

//    Server prodServer = new Server();
//    prodServer.setUrl(prodUrl);
//    prodServer.setDescription("Server URL in Production environment");

    Contact contact = new Contact();
    contact.setEmail("dietmar.glachs@salzburgresearch.at");
    contact.setName("Dietmar Glachs");
    contact.setUrl("https://www.salzburgresearch.at");

    License apacheLicense = new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0");

    Info info = new Info()
        .title("Asset Network API")
        .version("1.0")
        .contact(contact)
        .description("This API exposes endpoints for Asset Networks.").termsOfService("https://www.salzburgresearch.at")
        .license(apacheLicense);

    return new OpenAPI().info(info).servers(List.of(devServer));
  }
}
