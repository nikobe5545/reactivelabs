package se.beis.reactivelabs.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.beis.reactivelabs.domain.Customer;


public interface CustomerService {

    Flux<Customer> getAllCustomers();

    Mono<Customer> getCustomer(String id);

    Flux<Customer> getCustomersByFirstName(String firstName);

    Mono<Customer> getCustomerByFirstNameAndLastName(String firstName, String lastName);

    void addNewCustomer(Customer customer);

    void deleteCustomer(String id);
}
