package br.com.vacinaja.service;

import br.com.vacinaja.model.PostoSaude;
import br.com.vacinaja.model.Usuario;
import br.com.vacinaja.model.Vacina;
import br.com.vacinaja.repository.AgendamentoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class AgendamentoServiceTest {

    private final AgendamentoRepository agendamentoRepository = Mockito.mock(AgendamentoRepository.class);
    private final AgendamentoService agendamentoService = new AgendamentoService(agendamentoRepository);

    @Test
    void agendar_DeveRejeitarPostoSemVacinaSelecionada() {
        Usuario usuario = new Usuario("Ana", 30);
        Vacina vacina = new Vacina("Gripe", "Influenza", 0, 100, 1, 365);
        vacina.setId(1L);
        PostoSaude posto = criarPostoPadrao();

        assertThrows(IllegalArgumentException.class, () ->
            agendamentoService.agendar(usuario, vacina, posto, LocalDate.now().plusDays(1), LocalTime.of(9, 0), null)
        );
    }

    @Test
    void agendar_DeveRejeitarHorarioForaDoFuncionamento() {
        Usuario usuario = new Usuario("Ana", 30);
        Vacina vacina = new Vacina("Gripe", "Influenza", 0, 100, 1, 365);
        vacina.setId(1L);
        PostoSaude posto = criarPostoPadrao();
        posto.adicionarVacinaDisponivel(vacina);

        assertThrows(IllegalArgumentException.class, () ->
            agendamentoService.agendar(usuario, vacina, posto, LocalDate.now().plusDays(1), LocalTime.of(20, 0), null)
        );
    }

    @Test
    void agendar_DeveRejeitarVacinaForaDaFaixaEtariaDoUsuario() {
        Usuario usuario = new Usuario("Ana", 30);
        Vacina vacina = new Vacina("Pneumo Senior", "Pneumonia", 60, 130, 1, 0);
        vacina.setId(1L);
        PostoSaude posto = criarPostoPadrao();
        posto.adicionarVacinaDisponivel(vacina);

        assertThrows(IllegalArgumentException.class, () ->
            agendamentoService.agendar(usuario, vacina, posto, LocalDate.now().plusDays(1), LocalTime.of(9, 0), null)
        );
    }

    private PostoSaude criarPostoPadrao() {
        PostoSaude posto = new PostoSaude();
        posto.setDiasFuncionamento("MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY,SUNDAY");
        posto.setHoraAbertura(LocalTime.of(8, 0));
        posto.setHoraFechamento(LocalTime.of(17, 0));
        return posto;
    }
}
