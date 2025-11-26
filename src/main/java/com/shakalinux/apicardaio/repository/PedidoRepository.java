package com.shakalinux.apicardaio.repository;

import com.shakalinux.apicardaio.model.Pedido;
import com.shakalinux.apicardaio.model.enums.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido,Long> {
    @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.itens WHERE p.id = :id")
    Optional<Pedido> buscarComItens(Long id);

    List<Pedido> findByStatus(StatusPedido status);
    @Query("SELECT DISTINCT p FROM Pedido p LEFT JOIN FETCH p.itens")
    List<Pedido> listarComItens();

}
