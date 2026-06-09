package br.com.vacinaja.service;

import br.com.vacinaja.model.PostoSaude;
import br.com.vacinaja.repository.PostoSaudeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostoService {
    private final PostoSaudeRepository postoRepository;

    public PostoService(PostoSaudeRepository postoRepository) {
        this.postoRepository = postoRepository;
    }

    public PostoSaude cadastrarPosto(PostoSaude posto) {
        return postoRepository.save(posto);
    }

    public List<PostoSaude> listarPostos() {
        return postoRepository.findAll();
    }

    public PostoSaude buscarPorId(Long id) {
        return postoRepository.findById(id).orElseThrow(() -> new RuntimeException("Posto nao encontrado"));
    }

    @Transactional
    public void excluirPosto(Long id) {
        PostoSaude posto = buscarPorId(id);
        posto.getVacinasDisponiveis().clear();
        postoRepository.delete(posto);
    }

    public List<PostoSaude> buscarPorRegiaoEVacina(String regiao, String vacinaNome) {
        List<PostoSaude> postos;
        if (regiao != null && !regiao.isEmpty()) {
            postos = postoRepository.findByRegiaoContainingIgnoreCase(regiao);
        } else {
            postos = postoRepository.findAll();
        }

        if (vacinaNome != null && !vacinaNome.isEmpty()) {
            return postos.stream()
                .filter(p -> p.getVacinasDisponiveis().stream()
                    .anyMatch(v -> v.getNome().toLowerCase().contains(vacinaNome.toLowerCase())))
                .collect(Collectors.toList());
        }
        return postos;
    }
}
