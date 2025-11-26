package com.shakalinux.apicardaio.controller;

import com.shakalinux.apicardaio.exception.BadRequestException;
import com.shakalinux.apicardaio.exception.NotFoundException;
import com.shakalinux.apicardaio.model.Categoria;
import com.shakalinux.apicardaio.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement; // Importante
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
@Tag(name = "Categorias", description = "Gestão das categorias dos produtos (ex: Pizzas, Bebidas, Lanches).")
public class CategoriaController {
    @Autowired
    private CategoriaService categoriaService;

    @Operation(
            summary = "Criar uma nova categoria",
            description = "Adiciona uma nova categoria ao sistema. **Requer permissão de ADMIN.**"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Nome da categoria ausente ou inválido"),
            @ApiResponse(responseCode = "401", description = "Não autorizado (Token ausente ou inválido)"),
            @ApiResponse(responseCode = "403", description = "Proibido (Usuário não tem permissão de ADMIN)")
    })
    @SecurityRequirement(name = "BearerAuth")
    @PostMapping
    public ResponseEntity<Categoria> criar(@RequestBody Categoria categoria) {

        if (categoria.getNome() == null || categoria.getNome().isBlank()) {
            throw new BadRequestException("Nome da categoria é obrigatório");
        }

        Categoria nova = categoriaService.save(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(nova);
    }



    @Operation(
            summary = "Listar todas as categorias",
            description = "Retorna uma lista completa de todas as categorias ativas no cardápio."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de categorias retornada com sucesso")
    })
    @GetMapping
    public List<Categoria> listar() {
        return categoriaService.findAll();
    }


    @Operation(
            summary = "Buscar categoria por ID",
            description = "Retorna uma categoria específica pelo seu ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria encontrada"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada para o ID fornecido")
    })
    @GetMapping("/{id}")
    public Categoria buscar(@PathVariable Long id) {
        try {
            return categoriaService.findById(id);
        } catch (Exception e) {
            throw new NotFoundException("Categoria ID " + id + " não encontrada");
        }
    }
}