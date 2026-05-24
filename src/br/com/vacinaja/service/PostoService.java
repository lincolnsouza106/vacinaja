package br.com.vacinaja.service;

import br.com.vacinaja.model.PostoSaude;
import br.com.vacinaja.model.Vacina;
import java.util.ArrayList;
import java.util.List;

public class PostoService {
    private List<PostoSaude> postos;

    public PostoService() {
        this.postos = new ArrayList<>();
    }

    public void cadastrarPosto(PostoSaude posto) {
        this.postos.add(posto);
        System.out.println("Posto cadastrado: " + posto.getNome());
    }

    public List<PostoSaude> listarPostos() {
        return this.postos;
    }

    // Busca aprimorada: Filtra por região e, opcionalmente, pelo nome da vacina
    public List<PostoSaude> buscarPorRegiaoEVacina(String regiao, String nomeVacinaDesejada) {
        List<PostoSaude> encontrados = new ArrayList<>();
        
        for (PostoSaude posto : postos) {
            // Verifica se a região bate
            if (posto.getRegiao().equalsIgnoreCase(regiao)) {
                
                // Se não informou vacina específica, traz todos da região
                if (nomeVacinaDesejada == null || nomeVacinaDesejada.trim().isEmpty()) {
                    encontrados.add(posto);
                } else {
                    // Se informou, verifica se o posto tem essa vacina na lista
                    for (Vacina v : posto.getVacinasDisponiveis()) {
                        if (v.getNome().equalsIgnoreCase(nomeVacinaDesejada)) {
                            encontrados.add(posto);
                            break; // Já achou a vacina, não precisa continuar olhando as outras deste posto
                        }
                    }
                }
            }
        }
        return encontrados;
    }
}