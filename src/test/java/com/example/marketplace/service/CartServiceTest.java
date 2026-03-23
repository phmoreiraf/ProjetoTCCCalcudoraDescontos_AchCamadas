package com.example.marketplace.service;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.marketplace.model.CartSelection;
import com.example.marketplace.model.CartSummary;
import com.example.marketplace.repository.InMemoryProductRepository;

class CartServiceTest {

    private CartService cartService;

    @BeforeEach
    void setUp() {
        cartService = new CartService(new InMemoryProductRepository());
    }

    @Test
    void shouldCalculateSubtotalWithoutDiscountWhenOnlyOneProductIsSelected() {
        List<CartSelection> selections = List.of(
                new CartSelection(4L, 1));

        CartSummary summary = cartService.buildSummary(selections);

        assertEquals(new BigDecimal("29.90"), summary.getSubtotal().setScale(2, RoundingMode.HALF_UP));
        assertEquals(new BigDecimal("2.00"), summary.getDiscountPercent().setScale(2, RoundingMode.HALF_UP));
        assertEquals(new BigDecimal("29.30"), summary.getTotal().setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    void shouldApplyQuantityDiscountWhenTwoProductsAreSelected() {
        List<CartSelection> selections = List.of(
                new CartSelection(4L, 1),
                new CartSelection(5L, 1));

        CartSummary summary = cartService.buildSummary(selections);

        assertEquals(new BigDecimal("89.80"), summary.getSubtotal().setScale(2, RoundingMode.HALF_UP));
        assertEquals(new BigDecimal("9.00"), summary.getDiscountPercent().setScale(2, RoundingMode.HALF_UP));
        assertEquals(new BigDecimal("81.72"), summary.getTotal().setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    void shouldApplyCategoryAndQuantityDiscountRespectingTheMaximumLimit() {
        List<CartSelection> selections = List.of(
                new CartSelection(2L, 1),
                new CartSelection(3L, 1),
                new CartSelection(1L, 1),
                new CartSelection(4L, 1));

        CartSummary summary = cartService.buildSummary(selections);

        assertEquals(new BigDecimal("399.60"), summary.getSubtotal().setScale(2, RoundingMode.HALF_UP));
        assertEquals(new BigDecimal("23.00"), summary.getDiscountPercent().setScale(2, RoundingMode.HALF_UP));
        assertEquals(new BigDecimal("307.69"), summary.getTotal().setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    void shouldApplyCategoryDiscountForCapinhaEvenWithoutQuantityDiscount() {
        List<CartSelection> selections = List.of(
                new CartSelection(1L, 1));

        CartSummary summary = cartService.buildSummary(selections);

        assertEquals(new BigDecimal("49.90"), summary.getSubtotal().setScale(2, RoundingMode.HALF_UP));
        assertEquals(new BigDecimal("3.00"), summary.getDiscountPercent().setScale(2, RoundingMode.HALF_UP));
        assertEquals(new BigDecimal("48.40"), summary.getTotal().setScale(2, RoundingMode.HALF_UP));
    }
}
