package br.com.vacinaja.service;

import br.com.vacinaja.model.Vacina;
import java.util.ArrayList;
import java.util.List;

public class VacinaService {
    private List<Vacina> vacinas;

    public VacinaService() {
        this.vacinas = new ArrayList<>();
    }

    public void cadastrarVacina(Vacina vacina) {
        this.vacinas.add(vacina);
        System.out.println("Vacina cadastrada: " + vacina.getNome());
    }

    public List<Vacina> listarVacinas() {
        return this.vacinas;
    }
    
    public List<Vacina> consultarPorIdade(int idade) {
        List<Vacina> recomendadas = new ArrayList<>();
        for (Vacina v : vacinas) {
            if (idade >= v.getIdadeMinima() && idade <= v.getIdadeMaxima()) {
                recomendadas.add(v);
            }
        }
        return recomendadas;
    }
}