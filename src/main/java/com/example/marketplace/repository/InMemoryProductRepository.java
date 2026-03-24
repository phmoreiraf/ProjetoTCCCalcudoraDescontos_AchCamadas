package com.example.marketplace.repository;

import com.example.marketplace.model.Product;
import com.example.marketplace.model.ProductCategory;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryProductRepository implements ProductRepository {

    private final List<Product> produtos = new ArrayList<>();

    public InMemoryProductRepository() {
        produtos.add(new Product(1L, "Capinha Premium", new BigDecimal("49.90"), ProductCategory.CAPINHA));
        produtos.add(new Product(2L, "Carregador Turbo 30W", new BigDecimal("119.90"), ProductCategory.CARREGADOR));
        produtos.add(new Product(3L, "Fone Bluetooth AirSound", new BigDecimal("199.90"), ProductCategory.FONE));
        produtos.add(new Product(4L, "Película 3D", new BigDecimal("29.90"), ProductCategory.PELICULA));
        produtos.add(new Product(5L, "Suporte Veicular Magnético", new BigDecimal("59.90"), ProductCategory.SUPORTE));
    }

    @Override
    public List<Product> buscarTodos() { return produtos; }

    @Override
    public Optional<Product> buscarPorId(Long id) {
        return produtos.stream().filter(produto -> produto.getId().equals(id)).findFirst();
    }
}
