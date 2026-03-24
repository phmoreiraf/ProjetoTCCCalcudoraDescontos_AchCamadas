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

    private final CartService servicoCarrinho;
    private final ProductService servicoProdutos;

    public CartController(CartService servicoCarrinho, ProductService servicoProdutos) {
        this.servicoCarrinho = servicoCarrinho;
        this.servicoProdutos = servicoProdutos;
    }

    @PostMapping("/cart/summary")
    public String calcularResumo(
            @RequestParam(name = "productId", required = false) List<Long> idsProdutos,
            @RequestParam(name = "quantity", required = false) List<Integer> quantidades,
            Model modelo) {

        List<CartSelection> selecoes = new ArrayList<>();

        if (idsProdutos != null && quantidades != null) {
            for (int i = 0; i < Math.min(idsProdutos.size(), quantidades.size()); i++) {
                Integer quantidade = quantidades.get(i);
                if (quantidade != null && quantidade > 0) {
                    selecoes.add(new CartSelection(idsProdutos.get(i), quantidade));
                }
            }
        }

        CartSummary resumo = servicoCarrinho.construirResumo(selecoes);
        modelo.addAttribute("products", servicoProdutos.listarTodos());
        modelo.addAttribute("summary", resumo);
        modelo.addAttribute("featurePending", true);

        return "index";
    }
}
