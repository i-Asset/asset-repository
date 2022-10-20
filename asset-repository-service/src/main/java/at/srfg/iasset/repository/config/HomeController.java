package at.srfg.iasset.repository.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.servers.ServerVariable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Home redirection to swagger api documentation 
 */
@Controller
@OpenAPIDefinition(
        servers = {
                @Server(url = "{protocol}://{servername}:{port}/{basePath}",
                        description = "Configurable Server URL",
                        variables = {
                                @ServerVariable(name = "protocol",
                                        description = "http | https",
                                        defaultValue = "http"),
                                @ServerVariable(name = "servername", description = "IP address or server name",
                                        defaultValue = "localhost"),
                                @ServerVariable(name = "port", description = "Server port",
                                        defaultValue = "8081"),
                                @ServerVariable(name = "basePath", defaultValue = "")
                        }),
                @Server(url = "https://iasset.salzburgresearch.at/repository-service/",
                        description = "Asset Repository Service on the i-Asset Staging Server")
        }
)
@RequestMapping(value = "/")
public class HomeController {
    @RequestMapping(value = "/")
    public String index() {
        System.out.println("/swagger-ui/index.html");
        return "redirect:/swagger-ui/index.html";
    }
}
