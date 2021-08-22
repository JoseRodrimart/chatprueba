package com.jose.chatprueba.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Data
@Entity @Table(name="chat")
public class Chat implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    @Column(name="fecha")
    private Date fecha;
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinTable(
            name="participantes_chat",
            joinColumns =  @JoinColumn(name="id_chat"),
            inverseJoinColumns = @JoinColumn(name = "id_usuario")
    )
    private Set<Usuario> usuarios;
    @JsonIgnore
    @OneToMany(mappedBy = "chat")
    private List<Mensaje> mensajes;

    public Chat() {
        fecha = new Date();
    }

    //Helpers
    public void agregaUsuario(Usuario u){
        if(usuarios == null) usuarios = new HashSet<Usuario>() {
        };
        usuarios.add(u);
    }

    @Override
    public String toString() {
        return "Chat{" +
                "id=" + id +
                ", fecha=" + fecha +
                '}';
    }
}
