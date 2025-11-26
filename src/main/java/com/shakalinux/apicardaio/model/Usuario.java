package com.shakalinux.apicardaio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idUsuario;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private String role;

}
