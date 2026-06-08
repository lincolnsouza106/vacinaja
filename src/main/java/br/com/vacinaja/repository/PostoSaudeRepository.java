package br.com.vacinaja.repository;

import br.com.vacinaja.model.PostoSaude;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostoSaudeRepository extends JpaRepository<PostoSaude, Long> {
    List<PostoSaude> findByRegiaoContainingIgnoreCase(String regiao);
}
