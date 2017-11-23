package se.beis.reactivelabs.infrastructure.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.beis.reactivelabs.domain.Customer;
import se.beis.reactivelabs.publisher.PublisherProvider;

import java.nio.charset.StandardCharsets;

public class NewCustomerWebSocketHandler implements WebSocketHandler {

    private Flux<WebSocketMessage> webSocketMessagePublisher;

    private ObjectMapper objectMapper = new ObjectMapper();

    public NewCustomerWebSocketHandler(PublisherProvider<Customer> customerPublisherProvider,
                                       DataBufferFactory dataBufferFactory) {
        this.webSocketMessagePublisher = customerPublisherProvider.getPublisher().map(customer -> {
            try {
                String json = objectMapper.writeValueAsString(customer);
                byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
                DataBuffer buffer = dataBufferFactory.wrap(bytes);
                return new WebSocketMessage(WebSocketMessage.Type.TEXT, buffer);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        return session.send(this.webSocketMessagePublisher);
    }
}
