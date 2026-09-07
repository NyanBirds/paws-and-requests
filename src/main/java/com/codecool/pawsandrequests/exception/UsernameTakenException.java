package com.codecool.pawsandrequests.exception;

public class UsernameTakenException extends RuntimeException {
    public UsernameTakenException(final String email) {
        super("Email already taken: " + email);
    }
}
