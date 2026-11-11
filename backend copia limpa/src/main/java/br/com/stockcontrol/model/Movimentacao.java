
package br.com.stockcontrol.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimentacoes")
public class Movimentacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(nullable = false, length = 10)
    private String tipo;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(name = "data_movimentacao", nullable = false)
    private LocalDateTime dataMovimentacao;

    public Movimentacao() {
    }

    @PrePersist
    public void definirData() {
        if (dataMovimentacao == null) {
            dataMovimentacao = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public LocalDateTime getDataMovimentacao() {
        return dataMovimentacao;
    }
}
