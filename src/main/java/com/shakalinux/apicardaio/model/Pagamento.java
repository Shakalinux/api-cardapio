package com.shakalinux.apicardaio.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.shakalinux.apicardaio.model.enums.StatusPagamento;
import com.shakalinux.apicardaio.model.enums.TipoPagamento;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "pedido_id")
    @JsonBackReference
    private Pedido pedido;

    @Enumerated(EnumType.STRING)
    private TipoPagamento tipo;

    @Enumerated(EnumType.STRING)
    private StatusPagamento status;

    private Double valor;

    private String codigoTransacaoFake;

    private LocalDateTime criadoEm;
}
