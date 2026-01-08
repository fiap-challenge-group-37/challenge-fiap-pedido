package com.fiap.pedido.pedido.adapters.out.persistence;

import com.fiap.pedido.pedido.domain.entities.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoJpaRepository extends JpaRepository<PedidoEntity, Long> {
    List<PedidoEntity> findByStatus(StatusPedido status);

    @Query("SELECT p FROM PedidoEntity p WHERE p.status <> :statusFinalizado " +
            "ORDER BY CASE p.status " +
            "WHEN : statusPronto THEN 1 " +
            "WHEN :statusEmPreparacao THEN 2 " +
            "WHEN :statusRecebido THEN 3 " +
            "ELSE 4 END, p.dataCriacao ASC")
    List<PedidoEntity> findPedidosNaoFinalizadosOrdenadosParaCozinha(
            @Param("statusFinalizado") StatusPedido statusFinalizado,
            @Param("statusPronto") StatusPedido statusPronto,
            @Param("statusEmPreparacao") StatusPedido statusEmPreparacao,
            @Param("statusRecebido") StatusPedido statusRecebido
    );

    List<PedidoEntity> findByStatusInOrderByDataCriacaoAsc(List<StatusPedido> statuses);

    Optional<PedidoEntity> findByExternalID(String externalID);
}