package br.com.vacinaja.repository;

import br.com.vacinaja.model.RegistroVacinacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistroVacinacaoRepository extends JpaRepository<RegistroVacinacao, Long> {
}
