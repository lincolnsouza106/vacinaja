package br.com.vacinaja.repository;

import br.com.vacinaja.model.Vacina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VacinaRepository extends JpaRepository<Vacina, Long> {
    List<Vacina> findByIdadeMinimaLessThanEqualAndIdadeMaximaGreaterThanEqual(int idadeMinima, int idadeMaxima);
}
