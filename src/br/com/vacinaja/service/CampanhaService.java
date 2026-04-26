package br.com.vacinaja.service;

import br.com.vacinaja.model.Campanha;
import java.util.ArrayList;
import java.util.List;

public class CampanhaService {
    private List<Campanha> campanhas;

    public CampanhaService() {
        this.campanhas = new ArrayList<>();
    }

    public void cadastrarCampanha(Campanha campanha) {
        this.campanhas.add(campanha);
        System.out.println("Campanha cadastrada: " + campanha.getNome());
    }

    public List<Campanha> listarCampanhas() {
        return this.campanhas;
    }
}