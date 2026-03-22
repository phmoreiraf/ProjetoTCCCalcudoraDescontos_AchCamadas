package com.example.marketplace.controller;

import com.example.marketplace.model.CartSelection;
import com.example.marketplace.model.CartSummary;
import com.example.marketplace.service.CartService;
import com.example.marketplace.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {

    private final CartService cartService;
    private final ProductService productService;

    public CartController(CartService cartService, ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }

    @PostMapping("/cart/summary")
    public String calculateSummary(
            @RequestParam(name = "productId", required = false) List<Long> productIds,
            @RequestParam(name = "quantity", required = false) List<Integer> quantities,
            Model model) {

        List<CartSelection> selections = new ArrayList<>();

        if (productIds != null && quantities != null) {
            for (int i = 0; i < Math.min(productIds.size(), quantities.size()); i++) {
                Integer quantity = quantities.get(i);
                if (quantity != null && quantity > 0) {
                    selections.add(new CartSelection(productIds.get(i), quantity));
                }
            }
        }

        CartSummary summary = cartService.buildSummary(selections);
        model.addAttribute("products", productService.listAll());
        model.addAttribute("summary", summary);
        model.addAttribute("featurePending", true);

        return "index";
    }
}
