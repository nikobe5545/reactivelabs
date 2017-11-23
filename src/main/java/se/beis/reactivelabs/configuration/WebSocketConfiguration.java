package se.beis.reactivelabs.configuration;

import io.netty.buffer.PooledByteBufAllocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import se.beis.reactivelabs.infrastructure.websocket.CustomerWebSocketHandler;
import se.beis.reactivelabs.infrastructure.websocket.NewCustomerWebSocketHandler;
import se.beis.reactivelabs.publisher.AddedCustomerPublisher;
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
    public DataBufferFactory dataBufferFactory() {
        return new NettyDataBufferFactory(new PooledByteBufAllocator());
    }

    @Bean
    public HandlerMapping webSocketMapping(CustomerRepository customerRepository,
                                           DataBufferFactory dataBufferFactory,
                                           AddedCustomerPublisher addedCustomerPublisher) {

        Map<String, WebSocketHandler> map = new HashMap<>();
        map.put("/stream/customers", new CustomerWebSocketHandler(customerRepository, dataBufferFactory));
        map.put("/stream/customerAdded", new NewCustomerWebSocketHandler(addedCustomerPublisher, dataBufferFactory));

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
