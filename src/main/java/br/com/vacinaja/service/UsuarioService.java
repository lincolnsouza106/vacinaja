package br.com.vacinaja.service;

import br.com.vacinaja.model.RegistroVacinacao;
import br.com.vacinaja.model.Usuario;
import br.com.vacinaja.model.Vacina;
import br.com.vacinaja.repository.RegistroVacinacaoRepository;
import br.com.vacinaja.repository.UsuarioRepository;
import br.com.vacinaja.repository.VacinaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final VacinaRepository vacinaRepository;
    private final RegistroVacinacaoRepository registroRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, VacinaRepository vacinaRepository, RegistroVacinacaoRepository registroRepository) {
        this.usuarioRepository = usuarioRepository;
        this.vacinaRepository = vacinaRepository;
        this.registroRepository = registroRepository;
    }

    public Usuario cadastrarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email.toLowerCase()).orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));
    }

    public boolean emailJaCadastrado(String email) {
        return usuarioRepository.existsByEmail(email.toLowerCase());
    }

    public RegistroVacinacao registrarVacinacao(RegistroVacinacao registro) {
        return registroRepository.save(registro);
    }

    public List<Vacina> verificarPendencias(Long usuarioId) {
        Usuario usuario = buscarPorId(usuarioId);
        List<Long> vacinasTomadas = usuario.getListaRegistroVacinacao().stream()
            .map(r -> r.getVacina().getId())
            .collect(Collectors.toList());

        List<Vacina> todasVacinas = vacinaRepository.findAll();
        return todasVacinas.stream()
            .filter(v -> usuario.getIdade() >= v.getIdadeMinima() && usuario.getIdade() <= v.getIdadeMaxima())
            .filter(v -> !vacinasTomadas.contains(v.getId()))
            .collect(Collectors.toList());
    }
}
