package com.example.marketplace.model;

import java.math.BigDecimal;
import java.util.List;

public class CartSummary {

    private List<CartItem> items;
    private BigDecimal subtotal;
    private BigDecimal discountPercent;
    private BigDecimal discountValue;
    private BigDecimal total;

    public CartSummary(List<CartItem> items, BigDecimal subtotal, BigDecimal discountPercent, BigDecimal discountValue, BigDecimal total) {
        this.items = items;
        this.subtotal = subtotal;
        this.discountPercent = discountPercent;
        this.discountValue = discountValue;
        this.total = total;
    }

    public List<CartItem> getItems() { return items; }
    public BigDecimal getSubtotal() { return subtotal; }
    public BigDecimal getDiscountPercent() { return discountPercent; }
    public BigDecimal getDiscountValue() { return discountValue; }
    public BigDecimal getTotal() { return total; }
}
