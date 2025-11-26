package com.shakalinux.apicardaio.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Estrutura completa dos dados necessários para criar um novo pedido no sistema.")
public class PedidoRequestDTO {

    @Schema(
            description = "Lista de todos os itens e suas respectivas quantidades a serem incluídas neste pedido. **Não pode ser vazia.**"
    )
    private List<PedidoItemRequestDTO> itens;
}