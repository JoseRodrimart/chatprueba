package com.jose.chatprueba.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data @NoArgsConstructor @AllArgsConstructor @SuperBuilder
public class CreateUsuarioDTO extends UsuarioDTO{
    private String pass;
    private String pass2;
}
