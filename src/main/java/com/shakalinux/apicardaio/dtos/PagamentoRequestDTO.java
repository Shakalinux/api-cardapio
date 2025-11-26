package com.shakalinux.apicardaio.dtos;

import com.shakalinux.apicardaio.model.enums.TipoPagamento;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Estrutura dos dados necessários para iniciar o processamento de um pagamento para um pedido.")
public class PagamentoRequestDTO {

    @Schema(
            description = "ID único do pedido ao qual este pagamento se refere.",
            example = "25"
    )
    private Long pedidoId;

    @Schema(
            description = "Tipo de pagamento escolhido pelo cliente (Ex: CARTAO_CREDITO, PIX, DINHEIRO).",
            example = "CARTAO_CREDITO"
    )
    private TipoPagamento tipoPagamento;
}