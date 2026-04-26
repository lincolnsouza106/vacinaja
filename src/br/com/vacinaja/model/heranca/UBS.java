package br.com.vacinaja.model.heranca;

import br.com.vacinaja.model.PostoSaude;

public class UBS extends PostoSaude {
    public UBS(int id, String nome, String endereco, String regiao, String horarioFuncionamento, String telefone) {
        super(id, nome, endereco, regiao, horarioFuncionamento, telefone);
    }
}