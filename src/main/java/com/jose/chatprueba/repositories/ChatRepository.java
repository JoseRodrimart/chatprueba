package com.jose.chatprueba.repositories;

import com.jose.chatprueba.models.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {

    @Query("select new map(c.id, count(cm)) " +
            "from Chat c join c.usuarios cu join c.mensajes cm " +
            "where cu.id = :id_usuario " +
            "and cm.visto=false " +
            "and cm.usuario.id <> :id_usuario " +
            "group by c.id")
    Optional<List<Map<Integer,Integer>>> chatsConMensajesSinLeer(@Param("id_usuario") Integer id_usuario);

    @Query("Select c from Chat c join c.usuarios cu where cu.id=:id_usuario")
    Optional<List<Chat>> buscaPorUsuario(@Param("id_usuario") Integer id_usuario);
    @Query("Select uc from Usuario u join u.chats uc where u.mail=:email")
    Optional<List<Chat>> buscaPorEmail(@Param("email") String email);

}
