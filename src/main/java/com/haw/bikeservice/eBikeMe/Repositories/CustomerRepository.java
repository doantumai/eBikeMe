package com.haw.bikeservice.eBikeMe.Repositories;

import com.haw.bikeservice.eBikeMe.Customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByLastName(String lastName);
}
