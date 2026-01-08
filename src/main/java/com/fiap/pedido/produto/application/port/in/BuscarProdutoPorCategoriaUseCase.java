package com.fiap.pedido.produto.application.port.in;

import com.fiap.pedido.produto.domain.entities.Categoria;
import com.fiap.pedido.produto.domain.entities.Produto;

import java.util.List;

public interface BuscarProdutoPorCategoriaUseCase {
    List<Produto> executar(Categoria categoria);
}