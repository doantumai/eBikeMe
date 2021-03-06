package com.haw.bikeservice.eBikeMe.Repositories;

import com.haw.bikeservice.eBikeMe.Customer.Customer;
import com.haw.bikeservice.eBikeMe.Customer.Gender;
import com.haw.bikeservice.eBikeMe.EBikeMeApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Tu Mai Doan (tumai.doan@haw-hamburg.de)
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = EBikeMeApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles(profiles = "testing")
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        this.customerRepository.deleteAll();

        Customer customer = new Customer("Tu Mai", "Doan",
                "stefan.sarstedt@haw-hamburg.de", Gender.FEMALE);

        customerRepository.save(customer);
    }

    @Test
    void getCustomerSuccess() {
        Optional<Customer> customer = customerRepository.findByLastName("Doan");
        assertThat(customer).isPresent();
    }
}