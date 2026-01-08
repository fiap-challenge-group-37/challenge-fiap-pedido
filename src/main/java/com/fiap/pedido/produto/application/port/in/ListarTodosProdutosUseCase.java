package com.fiap.pedido.produto.application.port.in;

import com.fiap.pedido.produto.domain.entities.Produto;

import java.util.List;

public interface ListarTodosProdutosUseCase {
    List<Produto> executar();
}