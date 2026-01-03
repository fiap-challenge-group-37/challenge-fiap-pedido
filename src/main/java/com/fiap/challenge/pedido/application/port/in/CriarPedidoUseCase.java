package com.fiap.challenge.pedido.application.port.in;

import com.fiap.challenge.pedido.adapters.in.http.dto.PedidoDTO;
import com.fiap.challenge.pedido.domain.entities.Pedido;

public interface CriarPedidoUseCase {
    Pedido executar(PedidoDTO pedidoDTO);
}