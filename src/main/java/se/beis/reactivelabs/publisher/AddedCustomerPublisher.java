package se.beis.reactivelabs.publisher;

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

    private Flux<Customer> publisher;

    public AddedCustomerPublisher(PublishSubscribeChannel incomingCustomersChannel) {

        this.publisher = Flux.create(fluxSink -> incomingCustomersChannel.subscribe(new CustomerMessageHandler(fluxSink)));
    }

    @Override
    public Flux<Customer> getPublisher() {
        return publisher;
    }

    class CustomerMessageHandler implements MessageHandler {
        FluxSink<Customer> fluxSink;

        CustomerMessageHandler(FluxSink<Customer> fluxSink) {
            this.fluxSink = fluxSink;
        }

        @Override
        public void handleMessage(Message<?> message) throws MessagingException {
            Customer customer = (Customer) message.getPayload();
            fluxSink.next(customer);
        }
    }
}
