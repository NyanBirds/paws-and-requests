package com.codecool.pawsandrequests.exception;

public class ShelterNotFoundException extends RuntimeException {
    public ShelterNotFoundException(final String orgNr) {
        super("Shelter not found: " + orgNr);
    }
}
