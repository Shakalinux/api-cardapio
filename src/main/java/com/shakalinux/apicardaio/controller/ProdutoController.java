package com.shakalinux.apicardaio.controller;

import com.shakalinux.apicardaio.exception.BadRequestException;
import com.shakalinux.apicardaio.exception.NotFoundException;
import com.shakalinux.apicardaio.model.Produto;
import com.shakalinux.apicardaio.service.CategoriaService;
import com.shakalinux.apicardaio.service.ProdutoService;
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
@RequestMapping("/produtos")
@Tag(name = "Produtos", description = "Endpoints públicos para visualização do Cardápio.")
public class ProdutoController {
    @Autowired
    private ProdutoService produtoService;

    @Operation(
            summary = "Buscar produto por ID",
            description = "Retorna os detalhes de um produto específico, acessível a qualquer usuário."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto encontrado e retornado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado para o ID fornecido")
    })
    @GetMapping("/{id}")
    public Produto findById(@PathVariable Long id){
        return produtoService.findById(id)
                .orElseThrow(() -> new NotFoundException("Produto ID " + id + " não encontrado"));
    }

    @Operation(
            summary = "Listar todos os produtos",
            description = "Retorna uma lista de todos os produtos ativos no cardápio."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso")
    })
    @GetMapping
    public List<Produto> listar() {
        return produtoService.findAll();
    }

    @Operation(
            summary = "Remover um produto (ADMIN)",
            description = "Exclui um produto do sistema. **Requer permissão de ADMIN.**"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Produto removido com sucesso (No Content)"),
            @ApiResponse(responseCode = "401", description = "Não autorizado (Token ausente ou inválido)"),
            @ApiResponse(responseCode = "403", description = "Proibido (Usuário não tem permissão de ADMIN)"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado para o ID fornecido")
    })
    @SecurityRequirement(name = "BearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> remover(@PathVariable Long id) {
        Produto produto = produtoService.findById(id)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado"));
        produtoService.delete(produto);
        return ResponseEntity.noContent().build();
    }
}