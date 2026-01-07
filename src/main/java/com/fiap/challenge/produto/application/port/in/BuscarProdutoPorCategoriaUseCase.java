package com.fiap.challenge.produto.application.port.in;

import com.fiap.challenge.produto.domain.entities.Categoria;
import com.fiap.challenge.produto.domain.entities.Produto;

import java.util.List;

public interface BuscarProdutoPorCategoriaUseCase {
    List<Produto> executar(Categoria categoria);
}