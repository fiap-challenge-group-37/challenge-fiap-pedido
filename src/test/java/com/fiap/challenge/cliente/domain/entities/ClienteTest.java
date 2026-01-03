package com.fiap.challenge.cliente.domain.entities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

class ClienteTest {

    @Test
    void deveLancarExcecaoQuandoCpfInvalido() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Cliente("12345678901", "Nome Teste", "teste@email.com", "ROLE_CLIENTE"));
    }

    @Test
    void deveCriarClienteComCpfValido() {
        Cliente cliente = new Cliente("52998224725", "João", "joao@email.com", "ROLE_CLIENTE");
        Assertions.assertEquals("João", cliente.getNome());
        Assertions.assertEquals("joao@email.com", cliente.getEmail());
        Assertions.assertEquals("52998224725", cliente.getCpf().getValue());
    }
}
