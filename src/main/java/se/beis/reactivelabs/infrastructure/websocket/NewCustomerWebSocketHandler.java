package se.beis.reactivelabs.infrastructure.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.reactivestreams.Publisher;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import se.beis.reactivelabs.domain.Customer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NewCustomerWebSocketHandler implements WebSocketHandler {

    private PublishSubscribeChannel customerChannel;

    private ObjectMapper objectMapper = new ObjectMapper();

    public NewCustomerWebSocketHandler(PublishSubscribeChannel customerChannel) {
        this.customerChannel = customerChannel;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        final String sessionId = session.getId();

        Map<String, MessageHandler> handlerMap = new ConcurrentHashMap<>();

        Flux<WebSocketMessage> publisher = Flux.create(fluxSink -> {
            handlerMap.put(sessionId, new CustomerMessageHandler(session, fluxSink));
            customerChannel.subscribe(handlerMap.get(sessionId));
        });

        publisher.doFinally(signalType -> {
            customerChannel.unsubscribe(handlerMap.get(sessionId));
            handlerMap.remove(sessionId);
        });


        return session.send(publisher);
    }

    class CustomerMessageHandler implements MessageHandler {
        WebSocketSession webSocketSession;
        FluxSink<WebSocketMessage> fluxSink;

        public CustomerMessageHandler(WebSocketSession webSocketSession, FluxSink<WebSocketMessage> fluxSink) {
            this.webSocketSession = webSocketSession;
            this.fluxSink = fluxSink;
        }

        @Override
        public void handleMessage(Message<?> message) throws MessagingException {
            try {
                Customer customer = (Customer) message.getPayload();
                WebSocketMessage wsMessage = webSocketSession.textMessage(objectMapper.writeValueAsString(customer));
                fluxSink.next(wsMessage);
            } catch (JsonProcessingException e) {
                throw new MessagingException("Error serializing Customer to json", e);
            }
        }
    }

}
