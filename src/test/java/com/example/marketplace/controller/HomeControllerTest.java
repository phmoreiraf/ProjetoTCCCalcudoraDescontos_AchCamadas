package com.example.marketplace.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.example.marketplace.model.Product;
import com.example.marketplace.model.ProductCategory;
import com.example.marketplace.service.ProductService;

/**
 * Testes unitários para o HomeController.
 * Utiliza MockMvc para simular requisições HTTP e Mockito para mockar dependências.
 */
@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    /**
     * Testa se a página inicial é carregada com sucesso e retorna a view correta
     */
    @Test
    void deveCarregarPaginaInicialComSucesso() throws Exception {
        // Arrange
        List<Product> produtos = Arrays.asList(
                new Product(1L, "Capinha Transparente", new BigDecimal("29.90"), ProductCategory.CAPINHA),
                new Product(2L, "Película de Vidro", new BigDecimal("19.90"), ProductCategory.PELICULA)
        );
        when(productService.listarTodos()).thenReturn(produtos);

        // Act & Assert
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("products", produtos));

        verify(productService, times(1)).listarTodos();
    }

    /**
     * Testa se a página inicial é carregada mesmo quando não há produtos
     */
    @Test
    void deveCarregarPaginaInicialSemProdutos() throws Exception {
        // Arrange
        when(productService.listarTodos()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("products", Collections.emptyList()));

        verify(productService, times(1)).listarTodos();
    }

    /**
     * Testa se o modelo contém os produtos corretos
     */
    @Test
    void deveAdicionarProdutosAoModelo() throws Exception {
        // Arrange
        Product produto1 = new Product(1L, "Carregador Rápido", new BigDecimal("49.90"), ProductCategory.CARREGADOR);
        Product produto2 = new Product(2L, "Fone de Ouvido", new BigDecimal("24.90"), ProductCategory.FONE);
        List<Product> produtos = Arrays.asList(produto1, produto2);
        
        when(productService.listarTodos()).thenReturn(produtos);

        // Act & Assert
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("products", produtos));
    }
}
