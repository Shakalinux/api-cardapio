package com.shakalinux.apicardaio.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.shakalinux.apicardaio.model.Categoria;
import com.shakalinux.apicardaio.service.CategoriaService;

import com.shakalinux.apicardaio.component.JwtUtil;
import com.shakalinux.apicardaio.service.UsuarioService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Arrays;



@WebMvcTest(CategoriaController.class)
public class CategoriaControllerTest {


    @MockitoBean
    private CategoriaService categoriaService;
    @MockitoBean
    private UsuarioService usuarioService;
    @MockitoBean
    private JwtUtil jwtUtil;
    @MockitoBean
    private org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

    @Autowired
    private MockMvc mockMvc;


    @Test
    @org.springframework.security.test.context.support.WithMockUser

    void listar_DeveRetornarStatus200EListaVazia() throws Exception {
        when(categoriaService.findAll()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/categorias")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

    }



    @Test
    @org.springframework.security.test.context.support.WithMockUser(roles = "ADMIN")
    void criar_DeveRetornarStatus201ParaSucesso() throws Exception {
        Categoria novaCategoria = new Categoria(null, "Bebidas");
        Categoria salvaCategoria = new Categoria(2L, "Bebidas");

        when(categoriaService.save(any(Categoria.class))).thenReturn(salvaCategoria);
        mockMvc.perform(post("/categorias")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\": \"Bebidas\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Bebidas"));
    }
}