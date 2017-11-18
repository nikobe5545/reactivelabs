package se.beis.reactivelabs.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import se.beis.reactivelabs.infrastructure.websocket.CustomerWebSocketHandler;
import se.beis.reactivelabs.infrastructure.websocket.NewCustomerWebSocketHandler;
import se.beis.reactivelabs.repository.CustomerRepository;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class WebSocketConfiguration {

    @Bean
    public PublishSubscribeChannel incomingCustomersChannel() {
        return new PublishSubscribeChannel();
    }

    @Bean
    public HandlerMapping webSocketMapping(CustomerRepository customerRepository,
                                           PublishSubscribeChannel incomingCustomersChannel) {

        Map<String, WebSocketHandler> map = new HashMap<>();
        map.put("/stream/customers", new CustomerWebSocketHandler(customerRepository));
        map.put("/stream/customerAdded", new NewCustomerWebSocketHandler(incomingCustomersChannel));

        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setUrlMap(map);
        mapping.setOrder(1);
        return mapping;
    }

    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}
