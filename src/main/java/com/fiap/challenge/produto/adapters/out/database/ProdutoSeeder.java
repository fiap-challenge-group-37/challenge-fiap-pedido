package com.fiap.challenge.produto.adapters.out.database;

import com.fiap.challenge.produto.domain.entities.Categoria;
import com.fiap.challenge.produto.domain.entities.Produto;
import com.fiap.challenge.produto.domain.port.ProdutoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;

@Component
public class ProdutoSeeder implements CommandLineRunner {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Override
    public void run(String... args) throws Exception {
        if (produtoRepository.count() == 0) {
            produtoRepository.saveAll(Arrays.asList(
                    new Produto(null, "Hambúrguer", Categoria.LANCHE, new BigDecimal("18.90"), "Hambúrguer artesanal com queijo e salada"),
                    new Produto(null, "Sanduíche de Frango", Categoria.LANCHE, new BigDecimal("16.50"), "Sanduíche de frango grelhado"),
                    new Produto(null, "Cheeseburguer Bacon", Categoria.LANCHE, new BigDecimal("20.00"), "Cheeseburguer com bacon crocante"),
                    new Produto(null, "X-Salada", Categoria.LANCHE, new BigDecimal("15.00"), "Pão, hambúrguer, queijo, alface, tomate"),
                    new Produto(null, "Veggie Burger", Categoria.LANCHE, new BigDecimal("19.00"), "Hambúrguer vegetariano com legumes"),
                    new Produto(null, "Batata Frita", Categoria.ACOMPANHAMENTO, new BigDecimal("9.00"), "Porção de batatas fritas crocantes"),
                    new Produto(null, "Onion Rings", Categoria.ACOMPANHAMENTO, new BigDecimal("10.00"), "Anéis de cebola empanados"),
                    new Produto(null, "Salada Caesar", Categoria.ACOMPANHAMENTO, new BigDecimal("13.00"), "Salada Caesar com croutons"),
                    new Produto(null, "Nuggets", Categoria.ACOMPANHAMENTO, new BigDecimal("11.00"), "Nuggets de frango (6 unidades)"),
                    new Produto(null, "Mandioca Frita", Categoria.ACOMPANHAMENTO, new BigDecimal("10.00"), "Porção de mandioca frita"),
                    new Produto(null, "Coca-Cola", Categoria.BEBIDA, new BigDecimal("6.00"), "Refrigerante lata 350ml"),
                    new Produto(null, "Suco de Laranja", Categoria.BEBIDA, new BigDecimal("8.00"), "Suco natural de laranja"),
                    new Produto(null, "Água", Categoria.BEBIDA, new BigDecimal("4.00"), "Água mineral sem gás"),
                    new Produto(null, "Guaraná", Categoria.BEBIDA, new BigDecimal("6.00"), "Refrigerante de guaraná 350ml"),
                    new Produto(null, "Cerveja", Categoria.BEBIDA, new BigDecimal("9.00"), "Cerveja long neck"),
                    new Produto(null, "Chá Gelado", Categoria.BEBIDA, new BigDecimal("7.00"), "Chá gelado de pêssego"),
                    new Produto(null, "Pudim", Categoria.SOBREMESA, new BigDecimal("7.00"), "Pudim de leite condensado"),
                    new Produto(null, "Brownie com Sorvete", Categoria.SOBREMESA, new BigDecimal("12.00"), "Brownie de chocolate com sorvete"),
                    new Produto(null, "Petit Gâteau", Categoria.SOBREMESA, new BigDecimal("14.00"), "Petit gâteau com sorvete"),
                    new Produto(null, "Sorvete", Categoria.SOBREMESA, new BigDecimal("8.00"), "Taça de sorvete 2 bolas"),
                    new Produto(null, "Salada de Frutas", Categoria.SOBREMESA, new BigDecimal("7.50"), "Salada de frutas frescas")
            ));
        }
    }
}