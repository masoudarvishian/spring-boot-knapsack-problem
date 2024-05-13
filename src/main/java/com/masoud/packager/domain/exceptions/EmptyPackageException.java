package com.masoud.packager.domain.exceptions;

public class EmptyPackageException extends RuntimeException {
    public EmptyPackageException(String message) {
        super(message);
    }
}
