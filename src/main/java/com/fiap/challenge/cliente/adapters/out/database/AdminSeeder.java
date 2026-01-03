package com.fiap.challenge.cliente.adapters.out.database;

import com.fiap.challenge.cliente.domain.entities.Cliente;
import com.fiap.challenge.cliente.domain.port.ClienteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder implements CommandLineRunner {

    private final ClienteRepository clienteRepository;

    public AdminSeeder(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public void run(String... args) {
        String cpfAdmin = "12345678909";
        if (!clienteRepository.existsByCpf(cpfAdmin)) {
            Cliente admin = new Cliente(
                    cpfAdmin,
                    "Administrador",
                    "admin@seusite.com",
                    "ROLE_ADMIN"
            );
            clienteRepository.save(admin);
            System.out.println("Conta admin padrão criada!");
        } else {
            System.out.println("Conta admin padrão já existe.");
        }
    }
}