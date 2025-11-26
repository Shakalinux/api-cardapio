package com.shakalinux.apicardaio.service;

import com.shakalinux.apicardaio.exception.BadRequestException;
import com.shakalinux.apicardaio.model.Usuario;
import com.shakalinux.apicardaio.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    public Optional<Usuario> findByNome(String nome) {
        return usuarioRepository.findByNome(nome);
    }

    public Usuario registrar(String username, String senha) {

        if (usuarioRepository.findByNome(username).isPresent()) {
            throw new BadRequestException("Usuário já existe");
        }

        Usuario user = new Usuario();
        user.setNome(username);
        user.setSenha(bCryptPasswordEncoder.encode(senha));
        user.setRole("ROLE_USER");

        return usuarioRepository.save(user);
    }

    public void registrarAdmin(String username, String senha) {
        if (usuarioRepository.findByNome(username).isPresent()) {
            throw new BadRequestException("Já existe um usuário com esse username");
        }

        Usuario user = new Usuario();
        user.setNome(username);
        user.setSenha(bCryptPasswordEncoder.encode(senha));
        user.setRole("ROLE_ADMIN");

        usuarioRepository.save(user);
    }



}
