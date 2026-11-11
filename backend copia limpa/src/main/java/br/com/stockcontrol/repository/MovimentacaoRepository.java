
package br.com.stockcontrol.repository;

import br.com.stockcontrol.model.Movimentacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimentacaoRepository
        extends JpaRepository<Movimentacao, Long> {

    List<Movimentacao> findAllByOrderByDataMovimentacaoDesc();
}
