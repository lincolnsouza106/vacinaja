package br.com.vacinaja.service;

import br.com.vacinaja.model.PostoSaude;
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

    public List<PostoSaude> buscarPorRegiao(String regiao) {
        List<PostoSaude> postosEncontrados = new ArrayList<>();
        for (PostoSaude p : postos) {
            if (p.getRegiao().equalsIgnoreCase(regiao)) {
                postosEncontrados.add(p);
            }
        }
        return postosEncontrados;
    }
}