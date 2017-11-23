package se.beis.reactivelabs.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import se.beis.reactivelabs.domain.Customer;

@Component
public class AddedCustomerPublisher implements PublisherProvider<Customer> {

    private ObjectMapper objectMapper = new ObjectMapper();

    private Flux<Customer> publisher;

    public AddedCustomerPublisher(PublishSubscribeChannel incomingCustomersChannel, DataBufferFactory dataBufferFactory) {
        this.publisher = Flux.create(fluxSink -> {
            incomingCustomersChannel.subscribe(new CustomerMessageHandler(fluxSink, dataBufferFactory));
        });
    }

    @Override
    public Flux<Customer> getPublisher() {
        return publisher;
    }

    class CustomerMessageHandler implements MessageHandler {
        FluxSink<Customer> fluxSink;
        DataBufferFactory dataBufferFactory;

        public CustomerMessageHandler(FluxSink<Customer> fluxSink, DataBufferFactory dataBufferFactory) {
            this.fluxSink = fluxSink;
            this.dataBufferFactory = dataBufferFactory;
        }

        @Override
        public void handleMessage(Message<?> message) throws MessagingException {
            Customer customer = (Customer) message.getPayload();
            fluxSink.next(customer);
        }
    }
}
