package com.codecool.pawsandrequests.exception;

import java.util.UUID;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(final UUID id, final String resource) {
        super(resource + " with ID " + id + " not found");
    }
}
