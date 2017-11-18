package se.beis.reactivelabs.service;

import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.beis.reactivelabs.domain.Customer;
import se.beis.reactivelabs.repository.CustomerRepository;

import javax.annotation.Resource;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Resource
    private CustomerRepository customerRepository;

    @Resource
    private PublishSubscribeChannel incomingCustomersChannel;

    @Override
    public Flux<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Mono<Customer> getCustomer(String id) {
        return customerRepository.findById(id);
    }

    @Override
    public Flux<Customer> getCustomersByFirstName(String firstName) {
        return customerRepository.findByFirstName(firstName);
    }

    @Override
    public Mono<Customer> getCustomerByFirstNameAndLastName(String firstName, String lastName) {
        return customerRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    @Override
    public void addNewCustomer(Customer customer) {
        Mono<Customer> customerMono = customerRepository.save(customer);
        customerMono.block();
        incomingCustomersChannel.send(new Message<Customer>() {
            @Override
            public Customer getPayload() {
                return customer;
            }

            @Override
            public MessageHeaders getHeaders() {
                return null;
            }
        });
    }

    @Override
    public void deleteCustomer(String id) {
        customerRepository.deleteById(id);
    }
}
