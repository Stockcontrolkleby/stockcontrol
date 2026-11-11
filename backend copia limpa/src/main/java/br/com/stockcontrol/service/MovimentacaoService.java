
package br.com.stockcontrol.service;

import br.com.stockcontrol.model.Movimentacao;
import br.com.stockcontrol.model.Produto;
import br.com.stockcontrol.repository.MovimentacaoRepository;
import br.com.stockcontrol.repository.ProdutoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class MovimentacaoService {

    private final MovimentacaoRepository movimentacaoRepository;
    private final ProdutoRepository produtoRepository;

    public MovimentacaoService(
            MovimentacaoRepository movimentacaoRepository,
            ProdutoRepository produtoRepository) {

        this.movimentacaoRepository = movimentacaoRepository;
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public Movimentacao registrar(
            Long produtoId,
            String tipo,
            Integer quantidade) {

        // Validar o ID do produto
        if (produtoId == null) {
            throw new IllegalArgumentException(
                "Informe o produto."
            );
        }

        // Validar a quantidade
        if (quantidade == null || quantidade <= 0) {
            throw new IllegalArgumentException(
                "A quantidade deve ser maior que zero."
            );
        }

        // Validar o tipo de movimentação
        if (tipo == null) {
            throw new IllegalArgumentException(
                "Informe o tipo de movimentação."
            );
        }

        String tipoNormalizado = tipo.trim()
                .toUpperCase(Locale.ROOT);

        if (!tipoNormalizado.equals("ENTRADA")
                && !tipoNormalizado.equals("SAIDA")) {

            throw new IllegalArgumentException(
                "Tipo de movimentação inválido."
            );
        }

        // Buscar produto com bloqueio para evitar
        // movimentações simultâneas conflitantes
        Produto produto = produtoRepository
                .buscarPorIdComBloqueio(produtoId)
                .orElseThrow(() -> new IllegalArgumentException(
                    "Produto não encontrado."
                ));

        int estoqueAtual = produto.getQuantidade();
        int novoEstoque;

        // Calcular a nova quantidade
        if (tipoNormalizado.equals("ENTRADA")) {

            novoEstoque = Math.addExact(
                estoqueAtual,
                quantidade
            );

        } else {

            // Impedir saída maior que o estoque
            if (quantidade > estoqueAtual) {
                throw new IllegalArgumentException(
                    "Estoque insuficiente para essa saída."
                );
            }

            novoEstoque = estoqueAtual - quantidade;
        }

        // Atualizar o estoque
        produto.setQuantidade(novoEstoque);
        produtoRepository.save(produto);

        // Registrar a movimentação no histórico
        Movimentacao movimentacao = new Movimentacao();

        movimentacao.setProduto(produto);
        movimentacao.setTipo(tipoNormalizado);
        movimentacao.setQuantidade(quantidade);

        return movimentacaoRepository.save(movimentacao);
    }
}
