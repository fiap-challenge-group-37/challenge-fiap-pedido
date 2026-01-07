package com.fiap.challenge.pedido.application.port.in;

import com.fiap.challenge.pedido.domain.entities.Pedido;

public interface BuscarPedidoPorIdUseCase {
    Pedido buscarPorId(Long id);
}