package com.fiap.challenge.cliente.domain.port;

import com.fiap.challenge.cliente.domain.entities.Cliente;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository {
    Cliente save(Cliente cliente);
    Optional<Cliente> findById(Long id);
    List<Cliente> findAll();
    void deleteById(Long id);
    Optional<Cliente> findByCpf(String cpf);
    boolean existsByCpf(String cpf);
}