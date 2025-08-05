package com.fiap.challenge.pedido.application.port.in;

import com.fiap.challenge.pedido.domain.entities.Pedido;

import java.util.List;
import java.util.Optional;

public interface ListarPedidosUseCase {
    List<Pedido> executar();
    List<Pedido> executar(Optional<String> status); // Para filtrar por status
    Pedido executar(String externalID); // Para filtrar por status
}