package com.jose.chatprueba.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("detallesUsuarioServices")
@RequiredArgsConstructor
public class DetallesUsuarioServices implements UserDetailsService {

    private final UsuarioServices usuarioServices;

    @Override
    public UserDetails loadUserByUsername(String nombre ) throws UsernameNotFoundException{
        return usuarioServices
                .buscaPorNombre(nombre)
                .orElseThrow(()->new UsernameNotFoundException(nombre + "no encontrado"));
    }

}
