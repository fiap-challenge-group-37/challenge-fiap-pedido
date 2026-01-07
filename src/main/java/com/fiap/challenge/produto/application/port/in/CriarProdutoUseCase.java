package com.fiap.challenge.produto.application.port.in;

import com.fiap.challenge.produto.adapters.in.http.dto.ProdutoDTO;
import com.fiap.challenge.produto.domain.entities.Produto;

public interface CriarProdutoUseCase {
    Produto executar(ProdutoDTO produtoDTO);
}