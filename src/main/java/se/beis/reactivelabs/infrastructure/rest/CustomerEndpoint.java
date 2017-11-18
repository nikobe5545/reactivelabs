package se.beis.reactivelabs.infrastructure.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.beis.reactivelabs.domain.Customer;
import se.beis.reactivelabs.service.CustomerService;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@RequestMapping("/rest")
public class CustomerEndpoint {

    @Resource
    private CustomerService customerService;

    @GetMapping("/customers")
    public Flux<Customer> getCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/customer/{id}")
    public Mono<Customer> getCustomer(@PathVariable String id) {
        return customerService.getCustomer(id);
    }

    @PostMapping(value = "/customer", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addNewCustomer(@RequestBody Customer customer) throws IOException {
        customerService.addNewCustomer(customer);
    }

    @DeleteMapping("/customer/{id}")
    public void deleteCustomer(@PathVariable String id) {
        customerService.deleteCustomer(id);
    }
}
