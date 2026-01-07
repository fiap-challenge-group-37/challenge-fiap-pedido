package com.fiap.challenge.produto.application.port.in;

import com.fiap.challenge.produto.adapters.in.http.dto.ProdutoDTO;
import com.fiap.challenge.produto.domain.entities.Produto;

// No longer returns Optional<Produto>
public interface AtualizarProdutoUseCase {
    Produto executar(Long id, ProdutoDTO produtoDTO);
}