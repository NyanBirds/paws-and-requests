package com.codecool.paws_and_requests.exception;

public class ShelterNotFoundException extends RuntimeException {
    public ShelterNotFoundException(final String orgNr) {
        super("Shelter not found: " + orgNr);
    }
}
