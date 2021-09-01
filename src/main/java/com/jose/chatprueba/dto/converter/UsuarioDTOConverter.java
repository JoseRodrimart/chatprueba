package com.jose.chatprueba.dto.converter;

import com.jose.chatprueba.dto.GetUsuarioDTO;
import com.jose.chatprueba.dto.GetUsuarioDTOToken;
import com.jose.chatprueba.models.Usuario;
import com.jose.chatprueba.security.UserRole;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UsuarioDTOConverter {
    //private final ModelMapper modelMapper;

    public GetUsuarioDTO convertToDTO(Usuario usuario){
        return GetUsuarioDTO.builder()
                .nombre(usuario.getNombre())
                .mail(usuario.getMail())
                .imagen(usuario.getImagen())
                .roles(usuario
                        .getRoles()
                        .stream()
                        .map(UserRole::name)
                        .collect(Collectors.toSet()))
                .build();
    }

    public GetUsuarioDTOToken convertToDTOWithToken(Usuario usuario, String token) {
        return  GetUsuarioDTOToken.builder()
                    .nombre(usuario.getNombre())
                    .mail(usuario.getMail())
                    .imagen(usuario.getImagen())
                    .roles(usuario
                        .getRoles()
                        .stream()
                        .map(UserRole::name)
                        .collect(Collectors.toSet()))
                    .token(token)
                    .build();
    }
}
