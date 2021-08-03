package com.jose.chatprueba.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.List;
import java.util.Set;
@Data @NoArgsConstructor
@Entity @Table(name="usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    @Column(name="nombre")
    private String nombre;
    @Column(name="pass")
    private String pass;
    @Column(name="mail")
    private String mail;
    @Column(name="imagen")
    private String imagen;
    @JsonIgnore
    @ManyToMany(/*cascade = { CascadeType.ALL },*/fetch = FetchType.LAZY, mappedBy = "usuarios")
    private Set<Chat> chats;
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, /*cascade = CascadeType.ALL ,*/ mappedBy = "usuario")
    private List<Mensaje> mensajes;

    public Usuario(String nombre, String pass, String mail) {
        this.nombre = nombre;
        this.pass = pass;
        this.mail = mail;
    }

    public Usuario(String nombre, String mail){
        this.nombre = nombre;
        this.mail = mail;
    }

    //Helpers
    public void agregaChat(Chat c){
        chats.add(c);
    }

    public void agregaMensaje(Mensaje m){
        mensajes.add(m);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", pass='" + pass + '\'' +
                ", mail='" + mail + '\'' +
                '}';
    }
}
