package com.fiap.challenge.produto.application.port.in;

import com.fiap.challenge.produto.domain.entities.Produto;
import com.fiap.challenge.produto.adapters.in.http.dto.ProdutoDTO; // Assumindo que o DTO existente será usado

public interface CriarProdutoUseCase {
    Produto executar(ProdutoDTO produtoDTO);
}