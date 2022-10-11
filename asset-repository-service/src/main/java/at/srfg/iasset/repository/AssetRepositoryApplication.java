package at.srfg.iasset.repository;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableAutoConfiguration
@EnableMongoRepositories
public class AssetRepositoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssetRepositoryApplication.class, args);
	}

}
