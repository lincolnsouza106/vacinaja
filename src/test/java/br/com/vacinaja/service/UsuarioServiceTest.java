package br.com.vacinaja.service;

import br.com.vacinaja.model.Usuario;
import br.com.vacinaja.model.Vacina;
import br.com.vacinaja.repository.RegistroVacinacaoRepository;
import br.com.vacinaja.repository.UsuarioRepository;
import br.com.vacinaja.repository.VacinaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private VacinaRepository vacinaRepository;

    @Mock
    private RegistroVacinacaoRepository registroRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void cadastrarUsuario_DeveSalvarERetornarUsuario() {
        Usuario u = new Usuario("Carlos", 30);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(u);

        Usuario salvo = usuarioService.cadastrarUsuario(u);

        assertNotNull(salvo);
        assertEquals("Carlos", salvo.getNome());
        assertEquals(30, salvo.getIdade());
    }

    @Test
    void buscarPorId_DeveRetornarUsuarioQuandoExistir() {
        Usuario u = new Usuario("Ana", 25);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));

        Usuario encontrado = usuarioService.buscarPorId(1L);

        assertNotNull(encontrado);
        assertEquals("Ana", encontrado.getNome());
    }

    @Test
    void verificarPendencias_DeveConsiderarApenasVacinasDaFaixaEtariaDoUsuario() {
        Usuario usuario = new Usuario("Pedro", 30);
        usuario.setId(1L);
        Vacina indicada = new Vacina("Gripe", "Influenza", 18, 59, 1, 365);
        indicada.setId(10L);
        Vacina foraDaIdade = new Vacina("Pneumo Senior", "Pneumonia", 60, 130, 1, 0);
        foraDaIdade.setId(20L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(vacinaRepository.findAll()).thenReturn(List.of(indicada, foraDaIdade));

        List<Vacina> pendentes = usuarioService.verificarPendencias(1L);

        assertEquals(1, pendentes.size());
        assertEquals("Gripe", pendentes.get(0).getNome());
    }
}
