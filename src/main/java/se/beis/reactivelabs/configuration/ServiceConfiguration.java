package se.beis.reactivelabs.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = "se.beis.reactivelabs")
@Import({MongoConfiguration.class, WebSocketConfiguration.class})
@PropertySource("classpath:application.properties")
public class ServiceConfiguration {
}
