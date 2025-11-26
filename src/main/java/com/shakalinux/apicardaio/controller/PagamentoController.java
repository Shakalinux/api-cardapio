package com.shakalinux.apicardaio.controller;

import com.shakalinux.apicardaio.dtos.PagamentoRequestDTO;
import com.shakalinux.apicardaio.dtos.PagamentoResponseDTO;
import com.shakalinux.apicardaio.exception.BadRequestException;
import com.shakalinux.apicardaio.service.PagamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pagamentos")
@Tag(name = "Pagamentos", description = "Endpoints para processamento e gestão de transações financeiras.")
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;

    @Operation(
            summary = "Processar um novo pagamento para um pedido",
            description = "Inicia o fluxo de pagamento para um pedido existente, validando o tipo de pagamento e os dados necessários."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pagamento processado e autorizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação (ex: ID do Pedido ou Tipo de Pagamento ausentes)"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado no sistema"),
            @ApiResponse(responseCode = "422", description = "Falha no Processamento (ex: cartão recusado, saldo insuficiente, erro na adquirente)")
    })
    @PostMapping
    public ResponseEntity<PagamentoResponseDTO> pagar(@RequestBody PagamentoRequestDTO dto) {

        if (dto.getPedidoId() == null) {
            throw new BadRequestException("O ID do pedido é obrigatório.");
        }

        if (dto.getTipoPagamento() == null) {
            throw new BadRequestException("O tipo de pagamento é obrigatório.");
        }

        PagamentoResponseDTO resp = pagamentoService.processarPagamento(dto);
        return ResponseEntity.ok(resp);
    }
}