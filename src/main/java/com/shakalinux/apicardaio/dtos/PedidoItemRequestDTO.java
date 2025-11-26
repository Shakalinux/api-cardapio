package com.shakalinux.apicardaio.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Estrutura de dados para um único item a ser incluído na criação de um pedido.")
public class PedidoItemRequestDTO {

    @Schema(
            description = "ID único do produto que está sendo solicitado (Ex: ID da Pizza Grande).",
            example = "5"
    )
    private Long produtoId;

    @Schema(
            description = "Quantidade desejada deste produto.",
            example = "2"
    )
    private Integer quantidade;
}