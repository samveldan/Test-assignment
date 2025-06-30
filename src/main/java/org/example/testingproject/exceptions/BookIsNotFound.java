package org.example.testingproject.exceptions;

public class BookIsNotFound extends RuntimeException {
    public BookIsNotFound(String message) {
        super(message);
    }
}
