package com.fiap.challenge.pedido.domain.port;

import com.fiap.challenge.pedido.domain.entities.Pedido;
import com.fiap.challenge.pedido.domain.entities.StatusPedido;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository {
    Pedido save(Pedido pedido);
    Optional<Pedido> findById(Long id);
    List<Pedido> findAll(); // Este método agora usa a ordenação da cozinha
    List<Pedido> findByStatus(StatusPedido status);
    List<Pedido> findByStatusInOrderByDataCriacaoAsc(List<StatusPedido> statuses);
}