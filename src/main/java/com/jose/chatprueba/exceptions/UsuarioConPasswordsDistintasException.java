package com.jose.chatprueba.exceptions;

public class UsuarioConPasswordsDistintasException extends RuntimeException {
    private static final long serialVersionUID = -6670506709100021923L;

    public UsuarioConPasswordsDistintasException() {
        super("Las contraseñas no coinciden");
    }
}
