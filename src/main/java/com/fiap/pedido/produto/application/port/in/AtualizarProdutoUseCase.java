package com.fiap.pedido.produto.application.port.in;

import com.fiap.pedido.produto.adapters.in.http.dto.ProdutoDTO;
import com.fiap.pedido.produto.domain.entities.Produto;

// No longer returns Optional<Produto>
public interface AtualizarProdutoUseCase {
    Produto executar(Long id, ProdutoDTO produtoDTO);
}