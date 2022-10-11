package at.srfg.iasset.repository.persistence.config;

import org.bson.Document;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Configuration
@EnableMongoRepositories(basePackages = "at.srfg.iasset.repository")
public class MongoConnector extends AbstractMongoClientConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "iasset.persistence.mongodb")
    MongoConfig getMongoConfig() {
        return new MongoConfig();
    }

    @Bean
    public MongoClient getClient() {
        return MongoClients.create(getMongoConfig().getUrl());
    }

    @Bean
    public MongoDatabase getDatabase() {
        return getClient().getDatabase(getMongoConfig().getName());
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
    	return new MongoTemplate(getClient(), getMongoConfig().getName());
    }
    
    public MongoCollection<Document> getCollection(String collectionName) {
        return getDatabase().getCollection(collectionName);
    }
    // for use with mongo-template
    
	@Override
	public MongoClient mongoClient() {
		return MongoClients.create(getMongoConfig().getUrl());
	}

	@Override
	protected String getDatabaseName() {
		return getMongoConfig().getName();
	}

}
