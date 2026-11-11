
package br.com.stockcontrol.controller;

import br.com.stockcontrol.model.Movimentacao;
import br.com.stockcontrol.service.MovimentacaoService;
import br.com.stockcontrol.repository.MovimentacaoRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/movimentacoes")
@CrossOrigin(origins = {
    "http://127.0.0.1:5500",
    "http://localhost:5500"
})
public class MovimentacaoController {

    private final MovimentacaoService movimentacaoService;
    private final MovimentacaoRepository movimentacaoRepository;

    public MovimentacaoController(
            MovimentacaoService movimentacaoService,
            MovimentacaoRepository movimentacaoRepository) {

        this.movimentacaoService = movimentacaoService;
        this.movimentacaoRepository = movimentacaoRepository;
    }

    // Registrar entrada ou saída de estoque
    @PostMapping
    public ResponseEntity<?> registrar(
            @RequestBody DadosMovimentacao dados) {

        try {
            Movimentacao movimentacao =
                    movimentacaoService.registrar(
                        dados.produtoId(),
                        dados.tipo(),
                        dados.quantidade()
                    );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Map.of(
                        "id", movimentacao.getId(),
                        "produtoId", movimentacao.getProduto().getId(),
                        "tipo", movimentacao.getTipo(),
                        "quantidade", movimentacao.getQuantidade(),
                        "dataMovimentacao",
                        movimentacao.getDataMovimentacao()
                    ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("erro", e.getMessage()));

        } catch (ArithmeticException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                        "erro",
                        "Quantidade excede o limite permitido."
                    ));
        }
    }

    // Consultar histórico de movimentações
    @GetMapping
    public ResponseEntity<?> listarHistorico() {

        var historico = movimentacaoRepository
                .findAllByOrderByDataMovimentacaoDesc()
                .stream()
                .map(movimentacao -> Map.of(
                    "id", movimentacao.getId(),
                    "produtoId", movimentacao.getProduto().getId(),
                    "produtoNome", movimentacao.getProduto().getNome(),
                    "tipo", movimentacao.getTipo(),
                    "quantidade", movimentacao.getQuantidade(),
                    "dataMovimentacao",
                    movimentacao.getDataMovimentacao()
                ))
                .toList();

        return ResponseEntity.ok(historico);
    }

    // Dados recebidos do frontend
    public record DadosMovimentacao(
        Long produtoId,
        String tipo,
        Integer quantidade
    ) {}
}
