package com.example.marketplace.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.marketplace.model.CategoriaProduto;
import com.example.marketplace.model.ItemCarrinho;
import com.example.marketplace.model.Produto;
import com.example.marketplace.model.ResumoCarrinho;
import com.example.marketplace.model.SelecaoCarrinho;
import com.example.marketplace.repository.ProdutoRepository;

@Service
public class ServicoCarrinho {

    private final ProdutoRepository repositorioProdutos;

    public ServicoCarrinho(ProdutoRepository repositorioProdutos) {
        this.repositorioProdutos = repositorioProdutos;
    }

    public ResumoCarrinho construirResumo(List<SelecaoCarrinho> selecoes) {

        List<ItemCarrinho> itens = new ArrayList<>();

        // =========================
        // Monta os itens do carrinho
        // =========================
        for (SelecaoCarrinho selecao : selecoes) {
            Produto produto = repositorioProdutos.buscarPorId(selecao.getProdutoId())
                    .orElseThrow(
                            () -> new IllegalArgumentException("Produto não encontrado: " + selecao.getProdutoId()));

            itens.add(new ItemCarrinho(produto, selecao.getQuantidade()));
        }

        // =========================
        // Calcula subtotal
        // =========================
        BigDecimal subtotal = itens.stream()
                .map(ItemCarrinho::calcularSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // =========================================================
        // 🔴 INÍCIO DA IMPLEMENTAÇÃO (REMOVER PARA O EXPERIMENTO)
        // =========================================================

        // Quantidade total de itens
        int quantidadeTotal = itens.stream()
                .mapToInt(ItemCarrinho::getQuantidade)
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

        for (ItemCarrinho item : itens) {
            CategoriaProduto categoria = item.getProduto().getCategoria();

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

        return new ResumoCarrinho(itens, subtotal, percentualDesconto, valorDesconto, total);
    }
}
