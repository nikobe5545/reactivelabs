package se.beis.reactivelabs.configuration;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(basePackages = "se.beis.reactivelabs.repository")
public class MongoConfiguration extends AbstractReactiveMongoConfiguration {

    @Bean
    @Override
    public MongoClient reactiveMongoClient() {
        return MongoClients.create("mongodb://reactivelabs:reactivelabs@localhost:27017/reactivelabs");
    }

    @Bean
    @Override
    protected String getDatabaseName() {
        return "reactivelabs";
    }
}
