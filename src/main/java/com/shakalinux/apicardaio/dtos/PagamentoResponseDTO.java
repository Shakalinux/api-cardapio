package com.shakalinux.apicardaio.dtos;

import com.shakalinux.apicardaio.model.enums.StatusPagamento;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Estrutura dos dados de resposta após o processamento de um pagamento.")
public class PagamentoResponseDTO {

    @Schema(
            description = "ID único do pedido relacionado ao pagamento.",
            example = "25"
    )
    private Long pedidoId;

    @Schema(
            description = "Status final da transação (Ex: APROVADO, REPROVADO, PENDENTE).",
            example = "APROVADO"
    )
    private StatusPagamento status;

    @Schema(
            description = "Mensagem descritiva sobre o resultado da transação.",
            example = "Pagamento aprovado com sucesso pela operadora."
    )
    private String mensagem;

    @Schema(
            description = "Código de referência ou hash da transação, gerado pelo sistema (simulando um código de adquirente).",
            example = "TXN_1234567890"
    )
    private String codigoTransacaoFake;
}