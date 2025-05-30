package com.fiap.challenge.cliente.adapters.out.persistence;

import com.fiap.challenge.cliente.domain.entities.Cliente;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "clientes")
public class ClienteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    public ClienteEntity() {}

    public ClienteEntity(String cpf, String nome, String email) {
        this.cpf = cpf;
        this.nome = nome;
        this.email = email;
    }

    public Cliente toDomain() {
        return new Cliente(this.cpf, this.nome, this.email);
    }

    public static ClienteEntity fromDomain(Cliente cliente) {
        return new ClienteEntity(
                cliente.getCpf().getValue(),
                cliente.getNome(),
                cliente.getEmail()
        );
    }
}