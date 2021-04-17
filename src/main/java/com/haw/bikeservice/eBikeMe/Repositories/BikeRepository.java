package com.haw.bikeservice.eBikeMe.Repositories;

import com.haw.bikeservice.eBikeMe.Bike.Bike;
import com.haw.bikeservice.eBikeMe.Customer.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BikeRepository extends JpaRepository<Bike, Long> {
    Optional<Bike> findByStatus(Status bikeStatus);

    Optional<Bike> findByName(String bikeName);
}
