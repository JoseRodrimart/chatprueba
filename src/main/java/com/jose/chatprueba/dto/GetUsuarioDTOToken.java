package com.jose.chatprueba.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper=true)
public class GetUsuarioDTOToken extends GetUsuarioDTO implements Serializable {
    private String token;

}
