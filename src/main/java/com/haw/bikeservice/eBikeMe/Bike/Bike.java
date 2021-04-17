package com.haw.bikeservice.eBikeMe.Bike;

import com.haw.bikeservice.eBikeMe.Customer.Status;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Bike {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    private Status status;

    public Bike(String name) {
        this.name = name;
        this.status = null;
    }

    public Bike(String name, Status status) {
        this.name = name;
        this.status = status;
    }

    public void setStatus(Status status){
        this.status = status;
    }
}
