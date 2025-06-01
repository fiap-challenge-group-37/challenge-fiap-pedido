package com.fiap.challenge.pedido.adapters.out.persistence;

import com.fiap.challenge.pedido.domain.entities.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoJpaRepository extends JpaRepository<PedidoEntity, Long> {
    List<PedidoEntity> findByStatus(StatusPedido status);
    @Query("SELECT p FROM PedidoEntity p WHERE p.status <> com.fiap.challenge.pedido.domain.entities.StatusPedido.FINALIZADO " +
            "ORDER BY CASE p.status " +
            "WHEN com.fiap.challenge.pedido.domain.entities.StatusPedido.PRONTO THEN 1 " +
            "WHEN com.fiap.challenge.pedido.domain.entities.StatusPedido.EM_PREPARACAO THEN 2 " +
            "WHEN com.fiap.challenge.pedido.domain.entities.StatusPedido.RECEBIDO THEN 3 " +
            "ELSE 4 END, p.dataCriacao ASC")
    List<PedidoEntity> findPedidosNaoFinalizadosOrdenadosParaCozinha();

    List<PedidoEntity> findByStatusInOrderByDataCriacaoAsc(List<StatusPedido> statuses);
}