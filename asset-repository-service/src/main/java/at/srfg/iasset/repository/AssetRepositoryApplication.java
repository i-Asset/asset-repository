package at.srfg.iasset.repository;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

import at.srfg.iasset.repository.api.dependency.SemanticLookup;

@SpringBootApplication
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
@EnableMongoRepositories
@EnableDiscoveryClient
@EnableAsync
@EnableFeignClients(clients = {
		// used for taxonomy integration
		SemanticLookup.class,
})

public class AssetRepositoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssetRepositoryApplication.class, args);
	}

}
