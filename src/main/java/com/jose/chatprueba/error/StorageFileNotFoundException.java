package com.jose.chatprueba.error;

public class StorageFileNotFoundException extends StorageException{

    private static final long serialVersionUID = -710592347797649832L;

    public StorageFileNotFoundException(String message) {
        super(message);
    }

    public StorageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
