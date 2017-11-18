package se.beis.reactivelabs.infrastructure.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.beis.reactivelabs.domain.Customer;
import se.beis.reactivelabs.repository.CustomerRepository;

public class CustomerWebSocketHandler implements WebSocketHandler {

    private CustomerRepository customerRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    public CustomerWebSocketHandler(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        Flux<Customer> customerFlux = customerRepository.findWithTailableCursorBy();

        Publisher<WebSocketMessage> publisher = customerFlux.map(customer -> {
            try {
                String json = objectMapper.writeValueAsString(customer);
                return session.textMessage(json);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });

        return session.send(publisher);
    }
}