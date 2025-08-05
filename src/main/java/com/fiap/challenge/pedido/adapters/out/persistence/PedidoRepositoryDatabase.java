package com.fiap.challenge.pedido.adapters.out.persistence;

import com.fiap.challenge.pedido.domain.entities.Pedido;
import com.fiap.challenge.pedido.domain.entities.StatusPedido;
import com.fiap.challenge.pedido.domain.port.PedidoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class PedidoRepositoryDatabase implements PedidoRepository {

    private final PedidoJpaRepository jpaRepository;

    public PedidoRepositoryDatabase(PedidoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    @Transactional
    public Pedido save(Pedido pedido) {
        PedidoEntity entity = PedidoEntity.fromDomain(pedido);
        PedidoEntity savedEntity = jpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    @Transactional
    public void saveQRCode(Pedido pedido) {
        PedidoEntity entity = jpaRepository.findById(pedido.getId())
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com ID: " + pedido.getId()));
        entity.setQrCode(pedido.getQrCode());
        entity.toDomain();
    }

    @Override
    public Optional<Pedido> findByExternalId(String externalID) {
        return jpaRepository.findByExternalID(externalID).map(PedidoEntity::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pedido> findById(Long id) {
        return jpaRepository.findById(id).map(PedidoEntity::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> findAll() {
        return jpaRepository.findPedidosNaoFinalizadosOrdenadosParaCozinha().stream()
                .map(PedidoEntity::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> findByStatus(StatusPedido status) {
        return jpaRepository.findByStatus(status).stream()
                .map(PedidoEntity::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> findByStatusInOrderByDataCriacaoAsc(List<StatusPedido> statuses) {
        List<PedidoEntity> entities = jpaRepository.findByStatusInOrderByDataCriacaoAsc(statuses);
        return entities.stream()
                .map(PedidoEntity::toDomain)
                .toList();
    }
}