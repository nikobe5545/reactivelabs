package se.beis.reactivelabs.infrastructure.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.beis.reactivelabs.domain.Customer;
import se.beis.reactivelabs.publisher.PublisherProvider;

@Component
public class NewCustomerWebSocketHandler implements WebSocketHandler {

    private Flux<WebSocketMessage> webSocketMessagePublisher;

    public NewCustomerWebSocketHandler(PublisherProvider<Customer> customerPublisherProvider) {
        this.webSocketMessagePublisher = customerPublisherProvider.getPublisher()
                .map(WebSocketUtil::customerToJson)
                .map(WebSocketUtil::jsonToWebSocketMessage)
                .publish()
                .autoConnect();
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        return session.send(this.webSocketMessagePublisher);
    }
}
