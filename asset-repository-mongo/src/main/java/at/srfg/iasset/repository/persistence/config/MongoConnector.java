package at.srfg.iasset.repository.persistence.config;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
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
@EnableMongoRepositories(basePackages = "at.srfg.iasset.repository.persistence")
public class MongoConnector extends AbstractMongoClientConfiguration {
	@Value("${spring.data.mongodb.uri}")
	private String uri;
	@Value("${spring.data.mongodb.database}")
	private String database;



    @Bean
    public MongoDatabase getDatabase() {
        return mongoClient().getDatabase(database);
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
    	return new MongoTemplate(mongoClient(), database);
    }
    
    public MongoCollection<Document> getCollection(String collectionName) {
        return getDatabase().getCollection(collectionName);
    }
    // for use with mongo-template
    
	@Override
	public MongoClient mongoClient() {
		return MongoClients.create(uri);
	}

	@Override
	protected String getDatabaseName() {
		return database;
	}

}
