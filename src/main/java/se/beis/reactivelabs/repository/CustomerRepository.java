package se.beis.reactivelabs.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.beis.reactivelabs.domain.Customer;

public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {

    Flux<Customer> findByFirstName(String firstName);

    @Query("{ 'firstname': ?0, 'lastname': ?1}")
    Mono<Customer> findByFirstNameAndLastName(String firstname, String lastname);

    @Tailable
    Flux<Customer> findWithTailableCursorBy();
}
