package com.fiap.challenge.pedido.application.port.in;

import com.fiap.challenge.pedido.domain.entities.Pedido;

public interface AtualizarStatusPedidoUseCase {
    Pedido executar(Long pedidoId, String novoStatus);
}