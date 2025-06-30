package org.example.testingproject.exceptions;

public class AuthorIsNotFound extends RuntimeException{
    public AuthorIsNotFound(String message) {
        super(message);
    }
}
