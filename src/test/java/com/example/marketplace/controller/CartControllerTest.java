package com.example.marketplace.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.example.marketplace.model.CartItem;
import com.example.marketplace.model.CartSummary;
import com.example.marketplace.model.Product;
import com.example.marketplace.model.ProductCategory;
import com.example.marketplace.service.CartService;
import com.example.marketplace.service.ProductService;

/**
 * Testes unitários para o CartController.
 * Utiliza MockMvc para simular requisições HTTP e Mockito para mockar dependências.
 */
@SpringBootTest
@AutoConfigureMockMvc
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @MockBean
    private ProductService productService;

    /**
     * Testa o cálculo do resumo do carrinho com produtos selecionados
     */
    @Test
    void deveCalcularResumoComProdutosSelecionados() throws Exception {
        // Arrange
        Product produto = new Product(1L, "Capinha", new BigDecimal("29.90"), ProductCategory.CAPINHA);
        List<Product> produtos = Collections.singletonList(produto);
        
        CartItem item = new CartItem(produto, 2);
        CartSummary resumo = new CartSummary(
                Collections.singletonList(item),
                new BigDecimal("59.80"),
                new BigDecimal("5.00"),
                new BigDecimal("2.99"),
                new BigDecimal("56.81")
        );

        when(productService.listarTodos()).thenReturn(produtos);
        when(cartService.construirResumo(anyList())).thenReturn(resumo);

        // Act & Assert
        mockMvc.perform(post("/cart/summary")
                        .param("productId", "1")
                        .param("quantity", "2"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attributeExists("summary"))
                .andExpect(model().attributeExists("featurePending"))
                .andExpect(model().attribute("summary", resumo))
                .andExpect(model().attribute("featurePending", true));

        verify(cartService, times(1)).construirResumo(anyList());
        verify(productService, times(1)).listarTodos();
    }

    /**
     * Testa o cálculo do resumo com múltiplos produtos
     */
    @Test
    void deveCalcularResumoComMultiplosProdutos() throws Exception {
        // Arrange
        Product produto1 = new Product(1L, "Capinha", new BigDecimal("29.90"), ProductCategory.CAPINHA);
        Product produto2 = new Product(2L, "Película", new BigDecimal("19.90"), ProductCategory.PELICULA);
        List<Product> produtos = Arrays.asList(produto1, produto2);

        CartItem item1 = new CartItem(produto1, 1);
        CartItem item2 = new CartItem(produto2, 1);
        CartSummary resumo = new CartSummary(
                Arrays.asList(item1, item2),
                new BigDecimal("49.80"),
                new BigDecimal("5.00"),
                new BigDecimal("2.49"),
                new BigDecimal("47.31")
        );

        when(productService.listarTodos()).thenReturn(produtos);
        when(cartService.construirResumo(anyList())).thenReturn(resumo);

        // Act & Assert
        mockMvc.perform(post("/cart/summary")
                        .param("productId", "1", "2")
                        .param("quantity", "1", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("summary"))
                .andExpect(model().attribute("summary", resumo));

        verify(cartService, times(1)).construirResumo(anyList());
        verify(productService, times(1)).listarTodos();
    }

    /**
     * Testa o comportamento quando nenhum produto é selecionado
     */
    @Test
    void deveCalcularResumoSemProdutosSelecionados() throws Exception {
        // Arrange
        List<Product> produtos = Collections.emptyList();
        CartSummary resumoVazio = new CartSummary(
                Collections.emptyList(),
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );

        when(productService.listarTodos()).thenReturn(produtos);
        when(cartService.construirResumo(anyList())).thenReturn(resumoVazio);

        // Act & Assert
        mockMvc.perform(post("/cart/summary"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("summary"))
                .andExpect(model().attribute("summary", resumoVazio));

        verify(cartService, times(1)).construirResumo(Collections.emptyList());
        verify(productService, times(1)).listarTodos();
    }

    /**
     * Testa o comportamento quando quantidade é zero ou negativa (deve ser ignorada)
     */
    @Test
    void deveIgnorarProdutosComQuantidadeInvalida() throws Exception {
        // Arrange
        List<Product> produtos = Collections.emptyList();
        CartSummary resumo = new CartSummary(
                Collections.emptyList(),
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );

        when(productService.listarTodos()).thenReturn(produtos);
        when(cartService.construirResumo(anyList())).thenReturn(resumo);

        // Act & Assert - quantidade zero
        mockMvc.perform(post("/cart/summary")
                        .param("productId", "1")
                        .param("quantity", "0"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));

        // Act & Assert - quantidade negativa
        mockMvc.perform(post("/cart/summary")
                        .param("productId", "1")
                        .param("quantity", "-1"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));

        verify(cartService, times(2)).construirResumo(Collections.emptyList());
    }

    /**
     * Testa o comportamento quando os arrays de productId e quantity têm tamanhos diferentes
     */
    @Test
    void deveProcessarCorretamenteArraysComTamanhosDiferentes() throws Exception {
        // Arrange
        Product produto = new Product(1L, "Capinha", new BigDecimal("29.90"), ProductCategory.CAPINHA);
        List<Product> produtos = Collections.singletonList(produto);
        
        CartSummary resumo = new CartSummary(
                Collections.emptyList(),
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );

        when(productService.listarTodos()).thenReturn(produtos);
        when(cartService.construirResumo(anyList())).thenReturn(resumo);

        // Act & Assert - mais productIds que quantities
        mockMvc.perform(post("/cart/summary")
                        .param("productId", "1", "2", "3")
                        .param("quantity", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));

        verify(cartService).construirResumo(anyList());
    }

    /**
     * Testa se os atributos do modelo são configurados corretamente
     */
    @Test
    void deveConfigurarTodosAtributosDoModelo() throws Exception {
        // Arrange
        List<Product> produtos = new ArrayList<>();
        CartSummary resumo = new CartSummary(
                Collections.emptyList(),
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );

        when(productService.listarTodos()).thenReturn(produtos);
        when(cartService.construirResumo(anyList())).thenReturn(resumo);

        // Act & Assert
        mockMvc.perform(post("/cart/summary"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("products", produtos))
                .andExpect(model().attribute("summary", resumo))
                .andExpect(model().attribute("featurePending", true));
    }

    /**
     * Testa se o CartService é chamado com as seleções corretas
     */
    @Test
    void devePassarSelecoesCorretasParaCartService() throws Exception {
        // Arrange
        List<Product> produtos = Collections.emptyList();
        CartSummary resumo = new CartSummary(
                Collections.emptyList(),
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );

        when(productService.listarTodos()).thenReturn(produtos);
        when(cartService.construirResumo(any())).thenReturn(resumo);

        // Act
        mockMvc.perform(post("/cart/summary")
                        .param("productId", "1", "2")
                        .param("quantity", "3", "5"))
                .andExpect(status().isOk());

        // Assert - verificar que o service foi chamado com uma lista não vazia
        verify(cartService).construirResumo(argThat(list -> 
                list != null && list.size() == 2
        ));
    }
}
