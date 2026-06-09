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
        validarAgendamento(usuario, vacina, postoSaude, data, horario);
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

    private void validarAgendamento(Usuario usuario, Vacina vacina, PostoSaude postoSaude, LocalDate data, LocalTime horario) {
        if (data.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Nao e possivel agendar para uma data passada.");
        }
        if (usuario.getIdade() < vacina.getIdadeMinima() || usuario.getIdade() > vacina.getIdadeMaxima()) {
            throw new IllegalArgumentException("A vacina escolhida nao e indicada para a idade do usuario.");
        }
        if (!postoSaude.possuiVacina(vacina.getId())) {
            throw new IllegalArgumentException("O posto selecionado nao possui a vacina escolhida.");
        }
        if (!postoSaude.funcionaNoDia(data.getDayOfWeek())) {
            throw new IllegalArgumentException("O posto selecionado nao funciona nesse dia da semana.");
        }
        if (postoSaude.getHoraAbertura() != null && postoSaude.getHoraFechamento() != null
            && (horario.isBefore(postoSaude.getHoraAbertura()) || horario.isAfter(postoSaude.getHoraFechamento()))) {
            throw new IllegalArgumentException("O horario escolhido esta fora do funcionamento do posto.");
        }
    }
}
