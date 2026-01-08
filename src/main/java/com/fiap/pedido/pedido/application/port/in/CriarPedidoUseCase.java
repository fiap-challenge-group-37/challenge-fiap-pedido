package com.fiap.pedido.pedido.application.port.in;

import com.fiap.pedido.pedido.adapters.in.http.dto.PedidoDTO;
import com.fiap.pedido.pedido.domain.entities.Pedido;

public interface CriarPedidoUseCase {
    Pedido executar(PedidoDTO pedidoDTO);
}