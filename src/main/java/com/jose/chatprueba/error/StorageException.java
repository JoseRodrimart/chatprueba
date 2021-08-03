package com.jose.chatprueba.error;

public class StorageException extends RuntimeException{

    private static final long serialVersionUID = -3120193782558655566L;

    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
