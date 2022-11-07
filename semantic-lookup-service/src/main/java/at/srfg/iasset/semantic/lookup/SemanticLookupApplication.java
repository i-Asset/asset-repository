package at.srfg.iasset.semantic.lookup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
//github.com/i-Asset/eclass-service.git
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(exclude = { 
		SecurityAutoConfiguration.class , 
		ManagementWebSecurityAutoConfiguration.class
		})
@EnableDiscoveryClient
@EntityScan({
		// holds the "updatable" model 
		"at.srfg.iasset.semantic.model",
		// holds the E-Class Entities for data duplication - eClass is readonly
		"at.srfg.iasset.semantic.eclass.model"})
@ComponentScan({
		"at.srfg.iasset.semantic.eclass.repository",
		"at.srfg.iasset.semantic.eclass.service",
		"at.srfg.iasset.semantic" })
@EnableJpaRepositories({
		"at.srfg.iasset.semantic.eclass.repository",
		"at.srfg.iasset.semantic.lookup.repository"
})
@EnableAsync
//@EnableFeignClients(clients = {
//		SemanticIndexing.class
//})
@RestController
public class SemanticLookupApplication {

	public static void main(String[] args) {
		SpringApplication.run(SemanticLookupApplication.class, args);
	}

}
