package com.shakalinux.apicardaio.service;

import com.shakalinux.apicardaio.dtos.PedidoItemRequestDTO;
import com.shakalinux.apicardaio.dtos.PedidoRequestDTO;
import com.shakalinux.apicardaio.exception.NotFoundException;
import com.shakalinux.apicardaio.model.Categoria;
import com.shakalinux.apicardaio.model.ItemPedido;
import com.shakalinux.apicardaio.model.Pedido;
import com.shakalinux.apicardaio.model.Produto;
import com.shakalinux.apicardaio.model.enums.StatusPedido;
import com.shakalinux.apicardaio.repository.CategoriaRepository;
import com.shakalinux.apicardaio.repository.PedidoRepository;
import com.shakalinux.apicardaio.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;


    public Pedido criarPedido(PedidoRequestDTO dto) {

        Pedido pedido = new Pedido();
        pedido.setCriadoEm(LocalDateTime.now());
        pedido.setStatus(StatusPedido.AGUARDANDO_PAGAMENTO);

        List<ItemPedido> itens = new ArrayList<>();
        double total = 0.0;

        for (
                PedidoItemRequestDTO itemDto : dto.getItens()) {
            Produto produto = produtoRepository.findById(itemDto.getProdutoId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + itemDto.getProdutoId()));

            ItemPedido item = new ItemPedido();
            item.setProduto(produto);
            item.setNomeProduto(produto.getNome());
            item.setPrecoUnitario(produto.getPreco());
            item.setQuantidade(itemDto.getQuantidade());
            double subtotal = produto.getPreco() * itemDto.getQuantidade();
            item.setSubtotal(subtotal);
            item.setPedido(pedido);

            total += subtotal;
            itens.add(item);
        }

        pedido.setItens(itens);
        pedido.setValorTotal(total);

        return pedidoRepository.save(pedido);
    }
    @Transactional
    public Pedido buscarPorId(Long id) {
        return pedidoRepository.buscarComItens(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }

    public Pedido salvar(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public List<Pedido> listarTodos() {
        return pedidoRepository.listarComItens();
    }


    public Pedido cancelar(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pedido não encontrado"));

        pedido.setStatus(StatusPedido.CANCELADO);

        return pedidoRepository.save(pedido);
    }


    @Transactional
    public Pedido atualizarStatus(Long id, StatusPedido novoStatus) {
        Pedido pedido = pedidoRepository.buscarComItens(id)
                .orElseThrow(() -> new NotFoundException("Pedido não encontrado"));
        pedido.setStatus(novoStatus);
        return pedidoRepository.save(pedido);
    }

    public List<Pedido> listarPorStatus(StatusPedido status) {
        return pedidoRepository.findByStatus(status);
    }

}
