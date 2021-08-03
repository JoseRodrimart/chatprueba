package com.jose.chatprueba.dto.converter;

import com.jose.chatprueba.dto.UsuarioDTO;
import com.jose.chatprueba.models.Usuario;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsuarioDTOConverter {
    private final ModelMapper modelMapper;

    public UsuarioDTO convertToDTO(Usuario usuario){
        return modelMapper.map(usuario, UsuarioDTO.class);
    }
}
