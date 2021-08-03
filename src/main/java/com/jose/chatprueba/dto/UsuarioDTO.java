package com.jose.chatprueba.dto;

import lombok.*;
import org.springframework.stereotype.Component;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    private String nombre;
    private String mail;
    private String imagen;
}
