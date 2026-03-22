package com.example.marketplace.service;

import com.example.marketplace.model.CartItem;
import com.example.marketplace.model.CartSelection;
import com.example.marketplace.model.CartSummary;
import com.example.marketplace.model.Product;
import com.example.marketplace.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    private final ProductRepository productRepository;

    public CartService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public CartSummary buildSummary(List<CartSelection> selections) {
        List<CartItem> items = new ArrayList<>();

        for (CartSelection selection : selections) {
            Product product = productRepository.findById(selection.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + selection.getProductId()));

            items.add(new CartItem(product, selection.getQuantity()));
        }

        BigDecimal subtotal = items.stream()
                .map(CartItem::calculateSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // TODO: Implementar a feature do experimento.
        // Regras esperadas:
        // 1) desconto por quantidade:
        //    1 item = 0%
        //    2 itens = 5%
        //    3 itens = 10%
        //    4 ou mais = 15%
        // 2) desconto por categoria:
        //    CAPINHA = 5%
        //    CARREGADOR = 10%
        //    FONE = 8%
        //    demais = 0%
        // 3) o desconto total é cumulativo, mas limitado a 25%
        // 4) o total final = subtotal - valor do desconto
        //
        // Neste template inicial, a feature ainda NÃO foi implementada.
        // Por isso, os testes unitários devem falhar até que o aluno conclua a lógica.

        BigDecimal discountPercent = BigDecimal.ZERO;
        BigDecimal discountValue = BigDecimal.ZERO;
        BigDecimal total = subtotal;

        return new CartSummary(items, subtotal, discountPercent, discountValue, total);
    }
}
