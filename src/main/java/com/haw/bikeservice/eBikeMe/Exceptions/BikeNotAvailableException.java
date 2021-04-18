package com.haw.bikeservice.eBikeMe.Exceptions;

import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * @author Tu Mai Doan (tumai.doan@haw-hamburg.de)
 */
@Value
@EqualsAndHashCode(callSuper=false)
public class BikeNotAvailableException extends Exception{
    private final Long bikeId;

    private final String bikeName;

    public BikeNotAvailableException(Long bikeId) {
        super(String.format("Could not find bike with number %d.", bikeId));
        this.bikeId = bikeId;
        this.bikeName = "";
    }

    public BikeNotAvailableException(String bikeName) {
        super(String.format("This bike does not exist or is not available", bikeName));
        this.bikeName = bikeName;
        this.bikeId = 0L;
    }


}
