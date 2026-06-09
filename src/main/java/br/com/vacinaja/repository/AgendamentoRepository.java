package br.com.vacinaja.repository;

import br.com.vacinaja.model.Agendamento;
import br.com.vacinaja.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    List<Agendamento> findByUsuarioOrderByDataAscHorarioAsc(Usuario usuario);
    List<Agendamento> findAllByOrderByDataAscHorarioAsc();
}
