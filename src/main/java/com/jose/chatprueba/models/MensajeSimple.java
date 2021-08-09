package com.jose.chatprueba.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MensajeSimple {
    private String nombre;
    private String mensaje;
}
