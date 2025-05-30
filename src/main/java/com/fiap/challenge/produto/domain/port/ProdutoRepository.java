package com.fiap.challenge.produto.domain.port;

import com.fiap.challenge.produto.domain.entities.Produto;
import com.fiap.challenge.produto.domain.entities.Categoria;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository {
    Produto save(Produto produto);
    List<Produto> saveAll(List<Produto> produtos); // ADICIONADO
    Optional<Produto> findById(Long id);
    List<Produto> findAll();
    void deleteById(Long id);
    List<Produto> findByCategoria(Categoria categoria);
    long count();
}