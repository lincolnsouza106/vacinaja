package br.com.vacinaja.service;

import br.com.vacinaja.model.Campanha;
import br.com.vacinaja.repository.CampanhaRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CampanhaService {
    private final CampanhaRepository campanhaRepository;

    public CampanhaService(CampanhaRepository campanhaRepository) {
        this.campanhaRepository = campanhaRepository;
    }

    public Campanha cadastrarCampanha(Campanha campanha) {
        return campanhaRepository.save(campanha);
    }

    public List<Campanha> listarCampanhas() {
        return campanhaRepository.findAll();
    }
}