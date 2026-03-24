package com.example.marketplace.controller;

import com.example.marketplace.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final ProductService servicoProdutos;

    public HomeController(ProductService servicoProdutos) {
        this.servicoProdutos = servicoProdutos;
    }

    @GetMapping("/")
    public String index(Model modelo) {
        modelo.addAttribute("products", servicoProdutos.listarTodos());
        return "index";
    }
}
