package com.jose.chatprueba.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data @AllArgsConstructor @NoArgsConstructor @SuperBuilder
public class GetUsuarioDTO extends UsuarioDTO{
    protected Set<String> roles;
}
