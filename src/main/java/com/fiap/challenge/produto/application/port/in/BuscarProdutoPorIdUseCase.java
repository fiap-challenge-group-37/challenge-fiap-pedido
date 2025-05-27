package com.fiap.challenge.produto.application.port.in;

import com.fiap.challenge.produto.domain.entities.Produto;

public interface BuscarProdutoPorIdUseCase {
    Produto buscarPorId(Long id);
}