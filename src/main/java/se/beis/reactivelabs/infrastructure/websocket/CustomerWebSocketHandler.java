package se.beis.reactivelabs.infrastructure.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.beis.reactivelabs.repository.CustomerRepository;

@Component
public class CustomerWebSocketHandler implements WebSocketHandler {

    private Flux<WebSocketMessage> webSocketMessagePublisher;

    public CustomerWebSocketHandler(CustomerRepository customerRepository) {
        this.webSocketMessagePublisher = customerRepository.findWithTailableCursorBy()
                .map(WebSocketUtil::customerToJson)
                .map(WebSocketUtil::jsonToWebSocketMessage)
                .publish()
                .autoConnect();
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        return session.send(webSocketMessagePublisher);
    }
}