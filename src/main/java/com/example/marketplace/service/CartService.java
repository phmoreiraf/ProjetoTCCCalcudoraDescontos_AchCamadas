package com.example.marketplace.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.marketplace.model.CartItem;
import com.example.marketplace.model.CartSelection;
import com.example.marketplace.model.CartSummary;
import com.example.marketplace.model.Product;
import com.example.marketplace.model.ProductCategory;
import com.example.marketplace.repository.ProductRepository;

@Service
public class CartService {

    private final ProductRepository repositorioProdutos;

    public CartService(ProductRepository repositorioProdutos) {
        this.repositorioProdutos = repositorioProdutos;
    }

    public CartSummary construirResumo(List<CartSelection> selecoes) {

        List<CartItem> itens = new ArrayList<>();

        // =========================
        // Monta os itens do carrinho
        // =========================
        for (CartSelection selecao : selecoes) {
            Product produto = repositorioProdutos.buscarPorId(selecao.getProdutoId())
                    .orElseThrow(
                            () -> new IllegalArgumentException("Produto não encontrado: " + selecao.getProdutoId()));

            itens.add(new CartItem(produto, selecao.getQuantidade()));
        }

        // =========================
        // Calcula subtotal
        // =========================
        BigDecimal subtotal = itens.stream()
                .map(CartItem::calcularSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // =========================================================
        // 🔴 INÍCIO DA IMPLEMENTAÇÃO (REMOVER PARA O EXPERIMENTO)
        // =========================================================

        // Quantidade total de itens
        int quantidadeTotal = itens.stream()
                .mapToInt(CartItem::getQuantidade)
                .sum();

        // -------------------------
        // DESCONTO POR QUANTIDADE
        // -------------------------
        BigDecimal descontoQuantidade = BigDecimal.ZERO;

        if (quantidadeTotal == 2) {
            descontoQuantidade = new BigDecimal("5");
        } else if (quantidadeTotal == 3) {
            descontoQuantidade = new BigDecimal("7");
        } else if (quantidadeTotal >= 4) {
            descontoQuantidade = new BigDecimal("10");
        }

        // -------------------------
        // DESCONTO POR CATEGORIA
        // -------------------------
        BigDecimal descontoCategoria = BigDecimal.ZERO;

        for (CartItem item : itens) {
            ProductCategory categoria = item.getProduto().getCategoria();

            switch (categoria) {
                case CAPINHA:
                    descontoCategoria = descontoCategoria.add(new BigDecimal("3"));
                    break;
                case CARREGADOR:
                    descontoCategoria = descontoCategoria.add(new BigDecimal("5"));
                    break;
                case FONE:
                    descontoCategoria = descontoCategoria.add(new BigDecimal("3"));
                    break;
                case PELICULA:
                    descontoCategoria = descontoCategoria.add(new BigDecimal("2"));
                    break;
                case SUPORTE:
                    descontoCategoria = descontoCategoria.add(new BigDecimal("2"));
                    break;
                default:
                    break;
            }
        }

        // -------------------------
        // SOMA DOS DESCONTOS
        // -------------------------
        BigDecimal percentualDesconto = descontoQuantidade.add(descontoCategoria);

        // -------------------------
        // LIMITE MÁXIMO (25%)
        // -------------------------
        BigDecimal descontoMaximo = new BigDecimal("25");

        if (percentualDesconto.compareTo(descontoMaximo) > 0) {
            percentualDesconto = descontoMaximo;
        }

        // -------------------------
        // VALOR DO DESCONTO
        // -------------------------
        BigDecimal valorDesconto = subtotal
                .multiply(percentualDesconto)
                .divide(new BigDecimal("100"));

        // -------------------------
        // TOTAL FINAL
        // -------------------------
        BigDecimal total = subtotal.subtract(valorDesconto);

        // =========================================================
        // 🔴 FIM DA IMPLEMENTAÇÃO (REMOVER PARA O EXPERIMENTO)
        // =========================================================

        return new CartSummary(itens, subtotal, percentualDesconto, valorDesconto, total);
    }
}