package at.srfg.iasset.repository;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAutoConfiguration
@EnableMongoRepositories
@EnableDiscoveryClient
@EnableAsync
public class AssetRepositoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssetRepositoryApplication.class, args);
	}

}
