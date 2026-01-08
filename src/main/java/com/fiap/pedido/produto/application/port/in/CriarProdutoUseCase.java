package com.fiap.pedido.produto.application.port.in;

import com.fiap.pedido.produto.adapters.in.http.dto.ProdutoDTO;
import com.fiap.pedido.produto.domain.entities.Produto;

public interface CriarProdutoUseCase {
    Produto executar(ProdutoDTO produtoDTO);
}