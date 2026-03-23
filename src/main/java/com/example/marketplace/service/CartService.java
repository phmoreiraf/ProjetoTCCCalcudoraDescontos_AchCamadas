package com.example.marketplace.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.marketplace.model.CartItem;
import com.example.marketplace.model.CartSelection;
import com.example.marketplace.model.CartSummary;
import com.example.marketplace.model.Product;
import com.example.marketplace.model.ProductCategory;
import com.example.marketplace.repository.ProductRepository;

@Service
public class CartService {

    private final ProductRepository productRepository;

    public CartService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public CartSummary buildSummary(List<CartSelection> selections) {

        List<CartItem> items = new ArrayList<>();

        // =========================
        // Monta os itens do carrinho
        // =========================
        for (CartSelection selection : selections) {
            Product product = productRepository.findById(selection.getProductId())
                    .orElseThrow(
                            () -> new IllegalArgumentException("Produto não encontrado: " + selection.getProductId()));

            items.add(new CartItem(product, selection.getQuantity()));
        }

        // =========================
        // Calcula subtotal
        // =========================
        BigDecimal subtotal = items.stream()
                .map(CartItem::calculateSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // =========================================================
        // 🔴 INÍCIO DA IMPLEMENTAÇÃO (REMOVER PARA O EXPERIMENTO)
        // =========================================================

        // Quantidade total de itens
        int totalQuantity = items.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();

        // -------------------------
        // DESCONTO POR QUANTIDADE
        // -------------------------
        BigDecimal quantityDiscount = BigDecimal.ZERO;

        if (totalQuantity == 2) {
            quantityDiscount = new BigDecimal("5");
        } else if (totalQuantity == 3) {
            quantityDiscount = new BigDecimal("10");
        } else if (totalQuantity >= 4) {
            quantityDiscount = new BigDecimal("15");
        }

        // -------------------------
        // DESCONTO POR CATEGORIA
        // -------------------------
        BigDecimal categoryDiscount = BigDecimal.ZERO;

        for (CartItem item : items) {
            ProductCategory category = item.getProduct().getCategory();

            switch (category) {
                case CAPINHA:
                    categoryDiscount = categoryDiscount.add(new BigDecimal("5"));
                    break;
                case CARREGADOR:
                    categoryDiscount = categoryDiscount.add(new BigDecimal("10"));
                    break;
                case FONE:
                    categoryDiscount = categoryDiscount.add(new BigDecimal("8"));
                    break;
                default:
                    break;
            }
        }

        // -------------------------
        // SOMA DOS DESCONTOS
        // -------------------------
        BigDecimal discountPercent = quantityDiscount.add(categoryDiscount);

        // -------------------------
        // LIMITE MÁXIMO (25%)
        // -------------------------
        BigDecimal maxDiscount = new BigDecimal("25");

        if (discountPercent.compareTo(maxDiscount) > 0) {
            discountPercent = maxDiscount;
        }

        // -------------------------
        // VALOR DO DESCONTO
        // -------------------------
        BigDecimal discountValue = subtotal
                .multiply(discountPercent)
                .divide(new BigDecimal("100"));

        // -------------------------
        // TOTAL FINAL
        // -------------------------
        BigDecimal total = subtotal.subtract(discountValue);

        // =========================================================
        // 🔴 FIM DA IMPLEMENTAÇÃO (REMOVER PARA O EXPERIMENTO)
        // =========================================================

        return new CartSummary(items, subtotal, discountPercent, discountValue, total);
    }
}