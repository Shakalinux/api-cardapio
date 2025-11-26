package com.shakalinux.apicardaio.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String nomeProduto;
    private Double precoUnitario;
    private Integer quantidade;
    private Double subtotal;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    @JsonBackReference
    private Pedido pedido;


    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;
}
