package com.codecool.paws_and_requests.exception;

public class UsernameTakenException extends RuntimeException {
    public UsernameTakenException(final String email) {
        super("Email already taken: " + email);
    }
}
