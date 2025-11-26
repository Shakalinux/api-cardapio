package com.shakalinux.apicardaio.controller;

import com.shakalinux.apicardaio.exception.BadRequestException;
import com.shakalinux.apicardaio.model.Produto;
import com.shakalinux.apicardaio.service.CategoriaService;
import com.shakalinux.apicardaio.service.PedidoService;
import com.shakalinux.apicardaio.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/produtos")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Produtos (ADMIN)", description = "Gestão completa de Produtos: Criação, Atualização e Deleção. **Acesso restrito a ADMIN.**")
public class AdminProdutoController {
    private final ProdutoService produtoService;
    private final CategoriaService categoriaService;



    @Operation(
            summary = "Cadastrar novo produto",
            description = "Cria um novo item no cardápio, validando nome, preço e categoria. **Requer Bearer Token (ADMIN).**"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de produto inválidos (nome ou preço)"),
            @ApiResponse(responseCode = "401", description = "Não autorizado (Token ausente ou inválido)"),
            @ApiResponse(responseCode = "403", description = "Proibido (Usuário não tem a role ADMIN)")
    })
    @SecurityRequirement(name = "BearerAuth")
    @PostMapping
    public ResponseEntity<Produto> save(@RequestBody Produto produto) {

        if (produto.getNome() == null || produto.getNome().isBlank()) {
            throw new BadRequestException("Nome do produto é obrigatório");
        }

        if (produto.getPreco() == null || produto.getPreco() <= 0) {
            throw new BadRequestException("Preço inválido");
        }

        if (produto.getCategoria() != null) {

            var categoria = categoriaService.findById(produto.getCategoria().getIdCategoria());
            produto.setCategoria(categoria);
        }

        Produto salvo = produtoService.save(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }



    @Operation(
            summary = "Atualizar produto por ID",
            description = "Edita os dados de um produto existente no cardápio. **Requer Bearer Token (ADMIN).**"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de produto inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Proibido"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @SecurityRequirement(name = "BearerAuth")
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable Long id,
            @RequestBody Produto produtoAtualizado
    ) {
        return ResponseEntity.ok(produtoService.atualizar(id, produtoAtualizado));
    }



    @Operation(
            summary = "Alterar a categoria de um produto",
            description = "Move um produto para uma nova categoria. Útil para organização rápida do cardápio. **Requer Bearer Token (ADMIN).**"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria alterada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Proibido"),
            @ApiResponse(responseCode = "404", description = "Produto ou Categoria não encontrados")
    })
    @SecurityRequirement(name = "BearerAuth")
    @PutMapping("/{id}/categoria/{categoriaId}")
    public ResponseEntity<?> alterarCategoria(
            @PathVariable Long id,
            @PathVariable Long categoriaId
    ) {
        return ResponseEntity.ok(produtoService.trocarCategoria(id, categoriaId));
    }


    @Operation(
            summary = "Remover produto por ID",
            description = "Exclui permanentemente um produto do cardápio. **Requer Bearer Token (ADMIN).**"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto removido com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Proibido"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @SecurityRequirement(name = "BearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        produtoService.deletar(id);
        return ResponseEntity.ok("Produto removido com sucesso");
    }
}