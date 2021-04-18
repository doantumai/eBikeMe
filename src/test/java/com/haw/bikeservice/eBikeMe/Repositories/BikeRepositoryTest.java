package com.haw.bikeservice.eBikeMe.Repositories;

import com.haw.bikeservice.eBikeMe.Bike.Bike;
import com.haw.bikeservice.eBikeMe.Customer.Status;
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
class BikeRepositoryTest {

    @Autowired
    private BikeRepository bikeRepository;

    @BeforeEach
    void setUp() {
        this.bikeRepository.deleteAll();

        Bike bike1 = new Bike("Beantown Bikes", Status.AVAILABLE);
        bikeRepository.save(bike1);

        Bike bike2 = new Bike("Capital Bikes", Status.DEFECT);
        bikeRepository.save(bike2);
    }

    @Test
    void getBikeSuccess() {
        Optional<Bike> bike1 = bikeRepository.findByName("Beantown Bikes");
        assertThat(bike1).isPresent();

        Optional<Bike> bike2 = bikeRepository.findByStatus(Status.DEFECT);
        assertThat(bike2).isPresent();
    }
}