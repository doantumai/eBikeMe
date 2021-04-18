package com.haw.bikeservice.eBikeMe.Exceptions;

import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * @author Tu Mai Doan (tumai.doan@haw-hamburg.de)
 */
@Value
@EqualsAndHashCode(callSuper=false)
public class CustomerNotFoundException extends Exception {

    private final Long customerId;
    private final String lastName;

    CustomerNotFoundException(Long customerId) {
        super(String.format("Could not find customer with numer %d.", customerId));

        this.customerId = customerId;
        this.lastName = "";
    }

    public CustomerNotFoundException(String lastName) {
        super(String.format("Could not find customer with lastname %s.", lastName));

        this.customerId = 0L;
        this.lastName = lastName;
    }
}
