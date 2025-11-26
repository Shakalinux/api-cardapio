package com.shakalinux.apicardaio.utils;

import com.shakalinux.apicardaio.dtos.PedidoResponseDTO;
import com.shakalinux.apicardaio.model.Pedido;

import java.util.stream.Collectors;

public class PedidoMapper {

    public static PedidoResponseDTO toResponse(Pedido pedido) {

        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.setId(pedido.getId());
        dto.setStatus(pedido.getStatus());
        dto.setValorTotal(pedido.getValorTotal());

        dto.setItens(
                pedido.getItens().stream()
                        .map(item -> {
                            PedidoResponseDTO.ItemResumoDTO i = new PedidoResponseDTO.ItemResumoDTO();
                            i.setNomeProduto(item.getNomeProduto());
                            i.setQuantidade(item.getQuantidade());
                            i.setSubtotal(item.getSubtotal());
                            return i;
                        })
                        .collect(Collectors.toList())
        );

        return dto;
    }
}
