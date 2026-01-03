package com.fiap.challenge.cliente.domain.entities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

class CpfTest {

    @Test
    void deveCriarCpfValido() {
        Cpf cpf = new Cpf("52998224725");
        Assertions.assertEquals("52998224725", cpf.getValue());
    }

    @Test
    void deveLancarExcecao_ParaCpfInvalido() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Cpf("12345678901"));
    }

    @Test
    void deveLancarExcecao_ParaCpfNulo() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Cpf(null));
    }

    @Test
    void deveLancarExcecao_ParaCpfComDigitosRepetidos() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Cpf("11111111111"));
    }
}
