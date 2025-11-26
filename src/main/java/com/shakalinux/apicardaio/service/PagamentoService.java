package com.shakalinux.apicardaio.service;

import com.shakalinux.apicardaio.dtos.PagamentoRequestDTO;
import com.shakalinux.apicardaio.dtos.PagamentoResponseDTO;
import com.shakalinux.apicardaio.model.Pagamento;
import com.shakalinux.apicardaio.model.Pedido;
import com.shakalinux.apicardaio.model.enums.StatusPagamento;
import com.shakalinux.apicardaio.model.enums.StatusPedido;
import com.shakalinux.apicardaio.repository.CategoriaRepository;
import com.shakalinux.apicardaio.repository.PagamentoRepository;
import com.shakalinux.apicardaio.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PagamentoService {
    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private PagamentoRepository pagamentoRepository;

    public PagamentoResponseDTO processarPagamento(PagamentoRequestDTO request) {

        Pedido pedido = pedidoRepository.findById(request.getPedidoId())
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        if (pedido.getStatus() != StatusPedido.AGUARDANDO_PAGAMENTO) {
            throw new RuntimeException("Pedido não está aguardando pagamento.");
        }

        Pagamento pagamento = new Pagamento();
        pagamento.setPedido(pedido);
        pagamento.setTipo(request.getTipoPagamento());
        pagamento.setValor(pedido.getValorTotal());
        pagamento.setCriadoEm(LocalDateTime.now());


        pagamento.setStatus(StatusPagamento.APROVADO);
        pagamento.setCodigoTransacaoFake("FAKE-" + UUID.randomUUID());

        pagamentoRepository.save(pagamento);

        pedido.setStatus(StatusPedido.PAGO);
        pedido.setPagamento(pagamento);
        pedidoRepository.save(pedido);

        PagamentoResponseDTO resp = new PagamentoResponseDTO();
        resp.setPedidoId(pedido.getId());
        resp.setStatus(pagamento.getStatus());
        resp.setMensagem("Pagamento aprovado com sucesso.");
        resp.setCodigoTransacaoFake(pagamento.getCodigoTransacaoFake());

        return resp;
    }
}
