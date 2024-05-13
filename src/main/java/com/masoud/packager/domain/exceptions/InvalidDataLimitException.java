package com.masoud.packager.domain.exceptions;

public class InvalidDataLimitException extends RuntimeException{
    public InvalidDataLimitException(String message) {
        super(message);
    }
}
