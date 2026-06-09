package br.com.vacinaja.service;

import br.com.vacinaja.model.Agendamento;
import br.com.vacinaja.model.PostoSaude;
import br.com.vacinaja.model.Usuario;
import br.com.vacinaja.model.Vacina;
import br.com.vacinaja.repository.AgendamentoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class AgendamentoService {
    private final AgendamentoRepository agendamentoRepository;

    public AgendamentoService(AgendamentoRepository agendamentoRepository) {
        this.agendamentoRepository = agendamentoRepository;
    }

    public Agendamento agendar(Usuario usuario, Vacina vacina, PostoSaude postoSaude, LocalDate data, LocalTime horario, String observacao) {
        Agendamento agendamento = new Agendamento(usuario, vacina, postoSaude, data, horario, observacao);
        return agendamentoRepository.save(agendamento);
    }

    public List<Agendamento> listarTodos() {
        return agendamentoRepository.findAllByOrderByDataAscHorarioAsc();
    }

    public List<Agendamento> listarPorUsuario(Usuario usuario) {
        return agendamentoRepository.findByUsuarioOrderByDataAscHorarioAsc(usuario);
    }

    public Agendamento buscarPorId(Long id) {
        return agendamentoRepository.findById(id).orElseThrow(() -> new RuntimeException("Agendamento nao encontrado"));
    }

    public void cancelar(Long id) {
        Agendamento agendamento = buscarPorId(id);
        agendamento.setStatus("CANCELADO");
        agendamentoRepository.save(agendamento);
    }
}
