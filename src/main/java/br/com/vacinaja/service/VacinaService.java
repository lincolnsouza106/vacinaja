package br.com.vacinaja.service;

import br.com.vacinaja.model.Vacina;
import br.com.vacinaja.repository.VacinaRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class VacinaService {
    private final VacinaRepository vacinaRepository;

    public VacinaService(VacinaRepository vacinaRepository) {
        this.vacinaRepository = vacinaRepository;
    }

    public Vacina cadastrarVacina(Vacina vacina) {
        return vacinaRepository.save(vacina);
    }

    public List<Vacina> listarVacinas() {
        return vacinaRepository.findAll();
    }

    public Vacina buscarPorId(Long id) {
        return vacinaRepository.findById(id).orElseThrow(() -> new RuntimeException("Vacina nao encontrada"));
    }

    public List<Vacina> consultarPorIdade(int idade) {
        return vacinaRepository.findByIdadeMinimaLessThanEqualAndIdadeMaximaGreaterThanEqual(idade, idade);
    }
}
