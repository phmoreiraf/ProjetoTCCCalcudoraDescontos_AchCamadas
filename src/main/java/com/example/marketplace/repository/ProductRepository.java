package com.example.marketplace.repository;

import com.example.marketplace.model.Product;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    List<Product> buscarTodos();
    Optional<Product> buscarPorId(Long id);
}
