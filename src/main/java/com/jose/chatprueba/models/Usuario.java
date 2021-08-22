package com.jose.chatprueba.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jose.chatprueba.security.UserRole;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter @Setter
@NoArgsConstructor @Builder @AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="usuario")
public class Usuario implements UserDetails, Serializable {

    private static final long serialVersionUID = 131713468529657106L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    //@CreatedDate
    //private LocalDateTime fechaCreacion;

    //@Builder.Default
    //private LocalDateTime ultimaEdicionPass = LocalDateTime.now();


    //Relaciones
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="roles_usuario",
            joinColumns = @JoinColumn(name="id_usuario"))
    @Column(name="rol")
    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles;

    @JsonIgnore
    @ManyToMany(/*cascade = { CascadeType.ALL },*/fetch = FetchType.LAZY, mappedBy = "usuarios")
    private Set<Chat> chats;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, /*cascade = CascadeType.ALL ,*/ mappedBy = "usuario")
    private List<Mensaje> mensajes;

    public Usuario(String nombre, String pass, String mail, String imagen) {
        this.nombre = nombre;
        this.pass = pass;
        this.mail = mail;
        this.imagen = imagen;
    }

    public Usuario(String nombre, String mail, String imagen){
        this.nombre = nombre;
        this.mail = mail;
        this.imagen = imagen;
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

    //Metodos de UserDetails

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles
                .stream()
                .map(rol-> new SimpleGrantedAuthority("ROLE_" + rol.name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return pass;
    }

    @Override
    public String getUsername() {
        return nombre;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
