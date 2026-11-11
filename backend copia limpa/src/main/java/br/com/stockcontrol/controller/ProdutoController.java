
package br.com.stockcontrol.controller;

import br.com.stockcontrol.model.Produto;
import br.com.stockcontrol.service.ProdutoService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@CrossOrigin(origins = {
    "http://127.0.0.1:5500",
    "http://localhost:5500"
})
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    // Cadastrar produto
    @PostMapping
    public ResponseEntity<Produto> cadastrar(@RequestBody Produto produto) {
        Produto novoProduto = produtoService.cadastrar(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto);
    }

    // Listar todos os produtos
    @GetMapping
    public ResponseEntity<List<Produto>> listarTodos() {
        return ResponseEntity.ok(produtoService.listarTodos());
    }

    // Buscar produto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.of(produtoService.buscarPorId(id));
    }

    // Atualizar produto
    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(
            @PathVariable Long id,
            @RequestBody Produto dados) {

        if (produtoService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(produtoService.atualizar(id, dados));
    }

    // Excluir produto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        if (produtoService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        produtoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
