package com.shakalinux.apicardaio.controller;

import com.shakalinux.apicardaio.TestSecurity.SecurityTest;
import com.shakalinux.apicardaio.handler.GlobalExceptionHandler;
import com.shakalinux.apicardaio.exception.NotFoundException;
import com.shakalinux.apicardaio.model.Pedido;
import com.shakalinux.apicardaio.model.enums.StatusPedido;
import com.shakalinux.apicardaio.service.PedidoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminPedidoController.class)
@Import({SecurityTest.class, GlobalExceptionHandler.class})
class AdminPedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PedidoService pedidoService;

    @MockitoBean
    private com.shakalinux.apicardaio.component.JwtFilter jwtFilter;

    @MockitoBean
    private com.shakalinux.apicardaio.component.JwtUtil jwtUtil;


    private static final String URL = "/admin/pedidos";



    @Test
    @WithMockUser(roles = "ADMIN")
    void listar_SemStatus_DeveRetornar200() throws Exception {
        when(pedidoService.listarTodos()).thenReturn(List.of(new Pedido()));

        mockMvc.perform(get(URL))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void listar_ComStatusValido_DeveRetornar200() throws Exception {
        when(pedidoService.listarPorStatus(StatusPedido.ENTREGUE))
                .thenReturn(List.of(new Pedido()));

        mockMvc.perform(get(URL).param("status", "ENTREGUE"))
                .andExpect(status().isOk());
    }





    @Test
    void listar_SemAutenticacao_DeveRetornar401() throws Exception {
        mockMvc.perform(get(URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void listar_RoleErrada_DeveRetornar403() throws Exception {
        mockMvc.perform(get(URL))
                .andExpect(status().isForbidden());
    }



    @Test
    @WithMockUser(roles = "ADMIN")
    void buscar_Encontrado_DeveRetornar200() throws Exception {
        when(pedidoService.buscarPorId(1L)).thenReturn(new Pedido());

        mockMvc.perform(get(URL + "/1"))
                .andExpect(status().isOk());
    }





    @Test
    @WithMockUser(roles = "ADMIN")
    void atualizarStatus_DeveRetornar200() throws Exception {
        when(pedidoService.atualizarStatus(1L, StatusPedido.ENTREGUE))
                .thenReturn(new Pedido());

        mockMvc.perform(put(URL + "/1/status")
                        .param("status", "ENTREGUE"))
                .andExpect(status().isOk());
    }



    @Test
    @WithMockUser(roles = "ADMIN")
    void cancelar_DeveRetornar200() throws Exception {
        when(pedidoService.cancelar(1L))
                .thenReturn(new Pedido());

        mockMvc.perform(put(URL + "/1/cancelar"))
                .andExpect(status().isOk());
    }


}
