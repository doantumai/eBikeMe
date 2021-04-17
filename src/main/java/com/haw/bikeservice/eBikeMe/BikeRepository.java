package com.haw.bikeservice.eBikeMe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BikeRepository extends JpaRepository<Bike, Long> {
    Optional<Bike> findByStatus(String bikeName);

    Optional<Bike> findByName(String bikeName);
}
