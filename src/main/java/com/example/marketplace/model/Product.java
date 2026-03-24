package com.example.marketplace.model;

import java.math.BigDecimal;

public class Product {

    private Long id;
    private String nome;
    private BigDecimal preco;
    private ProductCategory categoria;

    public Product(Long id, String nome, BigDecimal preco, ProductCategory categoria) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.categoria = categoria;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public BigDecimal getPreco() { return preco; }
    public ProductCategory getCategoria() { return categoria; }
}
