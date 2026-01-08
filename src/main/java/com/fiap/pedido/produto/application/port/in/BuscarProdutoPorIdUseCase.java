package com.fiap.pedido.produto.application.port.in;

import com.fiap.pedido.produto.domain.entities.Produto;

public interface BuscarProdutoPorIdUseCase {
    Produto buscarPorId(Long id);
}