package com.fiap.challenge.produto.adapters.out.persistence;

import com.fiap.challenge.produto.domain.entities.Produto;
import com.fiap.challenge.produto.domain.entities.Categoria;
import com.fiap.challenge.produto.domain.port.ProdutoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ProdutoRepositoryDatabase implements ProdutoRepository {

    private final ProdutoJpaRepository jpaRepository;

    public ProdutoRepositoryDatabase(ProdutoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    @Transactional
    public Produto save(Produto produto) {
        ProdutoEntity entity = ProdutoEntity.fromDomain(produto);
        ProdutoEntity savedEntity = jpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    @Transactional
    public List<Produto> saveAll(List<Produto> produtos) {
        List<ProdutoEntity> entities = produtos.stream()
                .map(ProdutoEntity::fromDomain)
                .collect(Collectors.toList());
        List<ProdutoEntity> savedEntities = jpaRepository.saveAll(entities);
        return savedEntities.stream()
                .map(ProdutoEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Produto> findById(Long id) {
        return jpaRepository.findById(id).map(ProdutoEntity::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Produto> findAll() {
        return jpaRepository.findAll().stream()
                .map(ProdutoEntity::toDomain)
                .toList();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Produto> findByCategoria(Categoria categoria) {
        return jpaRepository.findByCategoria(categoria).stream()
                .map(ProdutoEntity::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return jpaRepository.count();
    }
}