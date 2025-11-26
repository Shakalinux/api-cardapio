package com.shakalinux.apicardaio.dtos;

import com.shakalinux.apicardaio.model.enums.StatusPedido;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Estrutura completa dos dados retornados para a visualização de um pedido.")
public class PedidoResponseDTO {

    @Schema(
            description = "ID único e identificador do pedido.",
            example = "25"
    )
    private Long id;

    @Schema(
            description = "Status atual do ciclo de vida do pedido (Ex: PENDENTE, EM_PREPARACAO, ENTREGUE).",
            example = "EM_PREPARACAO"
    )
    private StatusPedido status;

    @Schema(
            description = "Valor total final do pedido, incluindo todos os itens e possíveis taxas.",
            example = "89.90"
    )
    private Double valorTotal;

    @Schema(
            description = "Lista de itens contidos no pedido com informações de resumo e valor por item."
    )
    private List<ItemResumoDTO> itens;

    @Data
    @Schema(description = "Resumo dos detalhes de um item específico dentro do pedido.")
    public static class ItemResumoDTO {

        @Schema(
                description = "Nome do produto adquirido.",
                example = "Pizza Calabresa Grande"
        )
        private String nomeProduto;

        @Schema(
                description = "Quantidade solicitada deste produto.",
                example = "1"
        )
        private Integer quantidade;

        @Schema(
                description = "Subtotal (preço unitário * quantidade) para este item.",
                example = "49.90"
        )
        private Double subtotal;
    }
}