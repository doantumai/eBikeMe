package com.haw.bikeservice.eBikeMe.Customer;

import com.haw.bikeservice.eBikeMe.Bike.Bike;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String fistName;

    private String lastName;

    private String email;

    private Gender gender;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Setter(AccessLevel.NONE)
    private List<Bike> bikes = new ArrayList<>();

    public Customer(String lastName) {
        this.lastName = lastName;
        this.fistName = "";
        this.email = "";
        this.gender = null;
    }

    public Customer(String fistName, String lastName, String email, Gender gender) {
        this.fistName = fistName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
    }

    public void addBike(Bike bike){
        this.bikes.add(bike);
    }

    public List<Bike> getBikes() {
        return bikes;
    }
}
