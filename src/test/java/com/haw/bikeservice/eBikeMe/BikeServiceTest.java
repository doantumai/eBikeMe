package com.haw.bikeservice.eBikeMe;

import com.haw.bikeservice.eBikeMe.Bike.Bike;
import com.haw.bikeservice.eBikeMe.Customer.Customer;
import com.haw.bikeservice.eBikeMe.Customer.Gender;
import com.haw.bikeservice.eBikeMe.Customer.Status;
import com.haw.bikeservice.eBikeMe.Exceptions.BikeNotAvailableException;
import com.haw.bikeservice.eBikeMe.Exceptions.CustomerNotFoundException;
import com.haw.bikeservice.eBikeMe.Repositories.BikeRepository;
import com.haw.bikeservice.eBikeMe.Repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Tu Mai Doan (tumai.doan@haw-hamburg.de)
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = EBikeMeApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles(profiles = "testing")
class BikeServiceTest {

    @Autowired
    private BikeService bikeService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BikeRepository bikeRepository;

    @BeforeEach
    void setup(){
        // Customer
        this.customerRepository.deleteAll();

        Customer customer1 = new Customer("Tu Mai", "Doan",
                "tumai.doan@haw-hamburg.de", Gender.FEMALE);

        Customer customer2 = new Customer("Harry", "Porter",
                "harry.porter@haw-hamburg.de", Gender.MALE);

        customerRepository.save(customer1);
        customerRepository.save(customer2);

        //Bikes
        this.bikeRepository.deleteAll();

        Bike bike1 = new Bike("Beantown Bikes", Status.AVAILABLE);
        Bike bike2 = new Bike("Capital Bikes", Status.AVAILABLE);

        bikeRepository.save(bike1);
        bikeRepository.save(bike2);
    }

    @Test
    void getAllCustomersSuccess() {

        List<Customer> actual = bikeService.getCustomers();
        assertThat(actual).size().isEqualTo(2);
        assertThat(actual.get(0).getFirstName()).isEqualTo("Tu Mai");
    }

    @Test
    void getAllBikesSuccess(){

        List<Bike> actual = bikeService.getBikes();
        assertThat(actual).size().isEqualTo(2);
        assertThat(actual.get(0).getName()).isEqualTo("Beantown Bikes");
    }

    @Test
    void lendAndReturnBikeTest() throws BikeNotAvailableException, CustomerNotFoundException {

        bikeService.lendBike("Doan", "Beantown Bikes");
        bikeService.lendBike("Fischmann", "Capital Bikes");

        Bike bike = bikeRepository.findByName("Beantown Bikes").get();
        Customer customer = customerRepository.findByLastName("Doan").get();

        // Fahrrad ist erfolgreich hinzugefügt
        customer.getBikes().contains(bike);

        bikeService.returnBike("Doan", "Beantown Bikes");
        assertTrue(customerRepository.findByLastName("Doan").isPresent());

        //Farrad ist erfolgreich entfernt
        assertTrue(customerRepository.findByLastName("Doan").get().getBikes().size() == 0);

        Optional<Customer> customer3 = customerRepository.findByLastName("Fischmann");
        assertTrue(customer3.isPresent());

        // check if the exceptions are thrown
    }

    @Test
    void reportDefectTest() throws BikeNotAvailableException {
        // Bikes
        this.bikeRepository.deleteAll();

        Bike bike1 = new Bike("Beantown Bikes", Status.AVAILABLE);
        Bike bike2 = new Bike("Capital Bikes", Status.AVAILABLE);

        bikeRepository.save(bike1);
        bikeRepository.save(bike2);

        System.out.println("Beantown Bikes: " + bikeRepository.findByName("Beantown Bikes").get().getStatus());
        System.out.println("Capital Bikes: " + bikeRepository.findByName("Capital Bikes").get().getStatus());

        bikeService.reportDefect("Capital Bikes");
        System.out.println("report defect: " + bikeRepository.findByName("Capital Bikes").get().getStatus());

        assertTrue(bikeRepository.findByName("Capital Bikes").get().getStatus().equals(Status.DEFECT));

        bikeService.markStatusRepaired("Capital Bikes");
        System.out.println("repaired: " + bikeRepository.findByName("Capital Bikes").get().getStatus());
    }

    @Test
    void addAndRemoveBikeTest() throws  BikeNotAvailableException {
        // Bikes
        this.bikeRepository.deleteAll();

        bikeService.addBike("Peachtree Bikes", Status.AVAILABLE);
        bikeService.addBike("Bavaria Bikes", Status.AVAILABLE);

        Optional<Bike> bike1 = bikeRepository.findByName("Bavaria Bikes");
        Optional<Bike> bike2 = bikeRepository.findByName("Peachtree Bikes");

        // erfogreich hinzugefügt
        assertTrue(bike1.isPresent());
        assertTrue(bike2.isPresent());

        bikeService.removeBike("Peachtree Bikes");

        // erfolgreich entfernt
        assertFalse(bikeRepository.findByName("Peachtree Bikes").isPresent());
    }
}