package com.fiap.pedido.produto.domain.port;

import com.fiap.pedido.produto.domain.entities.Categoria;
import com.fiap.pedido.produto.domain.entities.Produto;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository {
    Produto save(Produto produto);
    List<Produto> saveAll(List<Produto> produtos);
    Optional<Produto> findById(Long id);
    List<Produto> findAll();
    void deleteById(Long id);
    List<Produto> findByCategoria(Categoria categoria);
    long count();
}