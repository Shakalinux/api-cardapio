package com.shakalinux.apicardaio.controller;

import com.shakalinux.apicardaio.exception.BadRequestException;
import com.shakalinux.apicardaio.model.Pedido;
import com.shakalinux.apicardaio.model.enums.StatusPedido;
import com.shakalinux.apicardaio.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/pedidos")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Pedidos (ADMIN)", description = "Gerenciamento e rastreamento de todos os pedidos no sistema. **Acesso restrito a ADMIN.**")
public class AdminPedidoController {

    private final PedidoService pedidoService;

    @Operation(
            summary = "Listar todos os pedidos, com filtro opcional por status",
            description = "Retorna todos os pedidos. Pode ser filtrado por um `status` específico (ex: PENDENTE, EM_PREPARACAO, ENVIADO). **Requer Bearer Token (ADMIN).**"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Status inválido fornecido no parâmetro"),
            @ApiResponse(responseCode = "401", description = "Não autorizado (Token ausente ou inválido)"),
            @ApiResponse(responseCode = "403", description = "Proibido (Usuário não tem a role ADMIN)")
    })
    @SecurityRequirement(name = "BearerAuth")
    @GetMapping
    public ResponseEntity<List<Pedido>> listar(
            @RequestParam(required = false) String status
    ) {
        if (status == null) {
            return ResponseEntity.ok(pedidoService.listarTodos());
        }

        try {
            return ResponseEntity.ok(
                    pedidoService.listarPorStatus(StatusPedido.valueOf(status.toUpperCase())) // Normalizando
            );
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Status de pedido inválido. Valores válidos: " + List.of(StatusPedido.values()));
        }

    }



    @Operation(
            summary = "Buscar detalhes de um pedido por ID",
            description = "Retorna os detalhes completos de um pedido específico. **Requer Bearer Token (ADMIN).**"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Proibido"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado para o ID fornecido")
    })
    @SecurityRequirement(name = "BearerAuth")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }



    @Operation(
            summary = "Atualizar o status do pedido",
            description = "Transiciona o pedido para um novo status dentro do fluxo (ex: PENDENTE -> EM_PREPARACAO -> ENVIADO). **Requer Bearer Token (ADMIN).**"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Status inválido ou não permitido para o fluxo atual"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Proibido"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    @SecurityRequirement(name = "BearerAuth")
    @PutMapping("/{id}/status")
    @Transactional
    public ResponseEntity<?> atualizarStatus(
            @PathVariable Long id,
            @RequestParam StatusPedido status) {

        Pedido atualizado = pedidoService.atualizarStatus(id, status);
        return ResponseEntity.ok(atualizado);
    }


    @Operation(
            summary = "Cancelar um pedido",
            description = "Define o status do pedido como CANCELADO. Deve ser usado com cautela, impedindo alterações futuras no pedido. **Requer Bearer Token (ADMIN).**"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido cancelado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Proibido"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "400", description = "Pedido já finalizado (não pode ser cancelado)")
    })
    @SecurityRequirement(name = "BearerAuth")
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelar(@PathVariable Long id) {
        Pedido pedido = pedidoService.cancelar(id);
        return ResponseEntity.ok(pedido);
    }
}