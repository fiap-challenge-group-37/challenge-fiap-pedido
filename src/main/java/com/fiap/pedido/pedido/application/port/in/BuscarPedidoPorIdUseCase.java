package com.fiap.pedido.pedido.application.port.in;

import com.fiap.pedido.pedido.domain.entities.Pedido;

public interface BuscarPedidoPorIdUseCase {
    Pedido buscarPorId(Long id);
}