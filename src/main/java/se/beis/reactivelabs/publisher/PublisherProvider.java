package se.beis.reactivelabs.publisher;

import reactor.core.publisher.Flux;

public interface PublisherProvider<T> {

    Flux<T> getPublisher();
}
