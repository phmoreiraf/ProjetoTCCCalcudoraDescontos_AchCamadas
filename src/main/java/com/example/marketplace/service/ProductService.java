package com.example.marketplace.service;

import com.example.marketplace.model.Product;
import com.example.marketplace.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repositorioProdutos;

    public ProductService(ProductRepository repositorioProdutos) {
        this.repositorioProdutos = repositorioProdutos;
    }

    public List<Product> listarTodos() {
        return repositorioProdutos.buscarTodos();
    }
}
