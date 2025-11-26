package com.shakalinux.apicardaio.TestService;

import com.shakalinux.apicardaio.exception.NotFoundException;
import com.shakalinux.apicardaio.model.Categoria;
import com.shakalinux.apicardaio.repository.CategoriaRepository;
import com.shakalinux.apicardaio.service.CategoriaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoriaServiceTest {
    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    private Categoria pizzaCategoria;

    @BeforeEach
    public void setUp() {
        pizzaCategoria = new Categoria(1L, "Pizza");
    }

    @Test
    void buscarPorId_DeveRetornarCategoriaQuuandoExistir(){
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(pizzaCategoria));
        Categoria resultado = categoriaService.findById(1L);
        assertNotNull(resultado);

        assertEquals("Pizza", resultado.getNome());
        verify(categoriaRepository, times(1)).findById(1L);
    }

    @Test
    void buscarPorId_DeveRetornarExcecaoQuandoNaoExistir(){
        long idNaoEncontrado = 99L;
        when(categoriaRepository.findById(idNaoEncontrado)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> {
            categoriaService.findById(idNaoEncontrado);
        });

        verify(categoriaRepository, times(1)).findById(idNaoEncontrado);
    }
}