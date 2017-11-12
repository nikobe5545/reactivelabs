package se.beis.reactivelabs.configuration;

import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(basePackages = "se.beis.reactivelabs")
@Import({MongoConfiguration.class, WebSocketConfiguration.class})
@PropertySource("classpath:application.properties")
public class ServiceConfiguration {
}
