package com.jose.chatprueba.repositories;

import com.jose.chatprueba.dto.UsuarioDTO;
import com.jose.chatprueba.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    @Query("SELECT u from Usuario u WHERE u.nombre = :nombre")
    Optional<Usuario> buscaPorNombre(@Param("nombre") String nombre);
    @Query("select u from Usuario u where u.mail = :email")
    Usuario buscaPorEmail(@Param("email") String email);
    @Query("select a " +
            "from Usuario a " +
            "inner join fetch a.chats b " +
            "join fetch b.usuarios c " +
            "where a.mail = :email " +
            "and c.id <> a.id")
    Usuario buscaPorEmailCompleto(@Param("email") String email);

    @Query("select new com.jose.chatprueba.models.Usuario(u.nombre, u.mail) from Usuario u")
    List<Usuario> buscaTodosDTO();
}
