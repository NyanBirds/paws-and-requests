package com.codecool.pawsandrequests.exception;

public class EmailTakenException extends RuntimeException {
    public EmailTakenException(final String email) {
        super("Email already taken: " + email);
    }
}
