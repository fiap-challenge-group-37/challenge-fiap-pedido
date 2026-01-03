package com.fiap.challenge.cliente.domain.entities;

import java.util.Objects;

public class Cpf {
    private final String value;

    public Cpf(String value) {
        if (!isValid(value)) {
            throw new IllegalArgumentException("CPF inválido");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static boolean isValid(String cpf) {
        if (cpf == null || cpf.length() != 11 || cpf.chars().distinct().count() == 1) return false;

        try {
            int d1 = 0, d2 = 0;
            for (int i = 0; i < 9; i++) {
                int dig = Character.getNumericValue(cpf.charAt(i));
                d1 += dig * (10 - i);
                d2 += dig * (11 - i);
            }

            d1 = 11 - (d1 % 11);
            if (d1 >= 10) d1 = 0;

            d2 += d1 * 2;
            d2 = 11 - (d2 % 11);
            if (d2 >= 10) d2 = 0;

            return d1 == Character.getNumericValue(cpf.charAt(9)) &&
                    d2 == Character.getNumericValue(cpf.charAt(10));
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cpf)) return false;
        Cpf cpf = (Cpf) o;
        return value.equals(cpf.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}