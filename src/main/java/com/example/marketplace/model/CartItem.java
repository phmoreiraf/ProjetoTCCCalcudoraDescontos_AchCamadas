package com.example.marketplace.model;

import java.math.BigDecimal;

public class CartItem {

    private Product produto;
    private int quantidade;

    public CartItem(Product produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public BigDecimal calcularSubtotal() {
        return produto.getPreco().multiply(BigDecimal.valueOf(quantidade));
    }

    public Product getProduto() { return produto; }
    public int getQuantidade() { return quantidade; }
}
