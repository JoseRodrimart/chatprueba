package com.jose.chatprueba.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data @AllArgsConstructor @NoArgsConstructor @SuperBuilder
public abstract class UsuarioDTO {
    protected String nombre;
    protected String mail;
    protected String imagen;
}
