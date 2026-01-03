package com.fiap.challenge.cliente.adapters.out.persistence;

import com.fiap.challenge.cliente.domain.entities.Cliente;
import com.fiap.challenge.cliente.domain.port.ClienteRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ClienteRepositoryDatabase implements ClienteRepository {

    private final ClienteJpaRepository springDataClienteJpaRepository;

    public ClienteRepositoryDatabase(ClienteJpaRepository springDataClienteJpaRepository) {
        this.springDataClienteJpaRepository = springDataClienteJpaRepository;
    }

    @Override
    public Optional<Cliente> findByCpf(String cpf) {
        return springDataClienteJpaRepository.findByCpf(cpf)
                .map(ClienteEntity::toDomain);
    }

    @Override
    public Cliente save(Cliente cliente) {
        ClienteEntity entity = ClienteEntity.fromDomain(cliente);
        ClienteEntity saved = springDataClienteJpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public List<Cliente> findAll() {
        return springDataClienteJpaRepository.findAll()
                .stream()
                .map(ClienteEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<Cliente> findById(Long id) {
        return springDataClienteJpaRepository.findById(id).map(ClienteEntity::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        springDataClienteJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByCpf(String cpf) {
        return springDataClienteJpaRepository.existsByCpf(cpf);
    }
}
