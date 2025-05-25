package com.fiap.challenge.produto.application.port.in;

import com.fiap.challenge.produto.domain.entities.Produto;
import java.util.List;

public interface ListarTodosProdutosUseCase {
    List<Produto> executar();
}