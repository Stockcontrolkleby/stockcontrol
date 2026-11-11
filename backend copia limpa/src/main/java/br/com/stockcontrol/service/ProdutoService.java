
package br.com.stockcontrol.service;

import br.com.stockcontrol.model.Produto;
import br.com.stockcontrol.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public Produto cadastrar(Produto produto) {
        return produtoRepository.save(produto);
    }

    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.findById(id);
    }

    public Produto atualizar(Long id, Produto dados) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        produto.setNome(dados.getNome());
        produto.setCategoria(dados.getCategoria());
        produto.setDescricao(dados.getDescricao());
        produto.setQuantidade(dados.getQuantidade());
        produto.setPreco(dados.getPreco());

        return produtoRepository.save(produto);
    }

    public void excluir(Long id) {
        produtoRepository.deleteById(id);
    }
}
