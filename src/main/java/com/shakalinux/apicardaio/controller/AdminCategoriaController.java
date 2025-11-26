package com.shakalinux.apicardaio.controller;

import com.shakalinux.apicardaio.model.Categoria;
import com.shakalinux.apicardaio.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/categorias")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Categorias (ADMIN)", description = "Gestão CRUD das categorias do cardápio. **Acesso restrito a ADMIN.**")
public class AdminCategoriaController {

    private final CategoriaService categoriaService;


    @Operation(
            summary = "Criar nova categoria",
            description = "Cadastra uma nova categoria para agrupar produtos (ex: 'Pizzas', 'Sucos'). **Requer Bearer Token (ADMIN).**"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados da categoria inválidos (ex: nome vazio)"),
            @ApiResponse(responseCode = "401", description = "Não autorizado (Token ausente ou inválido)"),
            @ApiResponse(responseCode = "403", description = "Proibido (Usuário não tem a role ADMIN)")
    })
    @SecurityRequirement(name = "BearerAuth")
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Categoria categoria) {

        return ResponseEntity.ok(categoriaService.save(categoria));
    }


    @Operation(
            summary = "Atualizar categoria por ID",
            description = "Altera os dados de uma categoria existente. **Requer Bearer Token (ADMIN).**"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Proibido"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    @SecurityRequirement(name = "BearerAuth")
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable Long id,
            @RequestBody Categoria categoria
    ) {
        return ResponseEntity.ok(categoriaService.atualizar(id, categoria));
    }


    @Operation(
            summary = "Remover categoria por ID",
            description = "Exclui permanentemente uma categoria do cardápio. **Requer Bearer Token (ADMIN).**"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria removida com sucesso (No Content)"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Proibido"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada"),
            @ApiResponse(responseCode = "400", description = "Erro de integridade (Categoria associada a produtos)")
    })
    @SecurityRequirement(name = "BearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> remover(@PathVariable Long id) {
        categoriaService.remover(id);
        return ResponseEntity.ok().build();
    }
}