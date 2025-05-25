package com.fiap.challenge.produto.application.port.in;

import com.fiap.challenge.produto.domain.entities.Produto;
import java.util.Optional; // Optional will be removed

// No longer returns Optional<Produto>
public interface BuscarProdutoPorIdUseCase {
    Produto buscarPorId(Long id);
}