package com.shakalinux.apicardaio.controller;

import com.shakalinux.apicardaio.TestSecurity.SecurityTest;

import com.shakalinux.apicardaio.component.JwtUtil;
import com.shakalinux.apicardaio.service.UsuarioService;
import com.shakalinux.apicardaio.model.Categoria;
import com.shakalinux.apicardaio.service.CategoriaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


@WebMvcTest(AdminCategoriaController.class)
@Import(SecurityTest.class)
@TestPropertySource(properties = "spring.security.enabled=true")
class AdminCategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoriaService categoriaService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UsuarioService usuarioService;


    @Test
    @WithMockUser(roles = "USER")
    void criar_ComRoleIncorreta_DeveRetornar403() throws Exception {
        mockMvc.perform(post("/admin/categorias")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\": \"Teste\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void criar_ComRoleCorreta_DeveRetornar201() throws Exception {
        Categoria categoriaSalva = new Categoria(1L, "Teste");

        when(categoriaService.save(any(Categoria.class))).thenReturn(categoriaSalva);

        mockMvc.perform(post("/admin/categorias")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\": \"Teste\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void criar_SemAutenticacao_DeveRetornar401() throws Exception {
        mockMvc.perform(post("/admin/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\": \"Teste\"}"))
                .andExpect(status().isUnauthorized());
    }


}
