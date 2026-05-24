package br.com.vacinaja.model.heranca;

import br.com.vacinaja.model.PostoSaude;

public class Farmacia extends PostoSaude {

    public Farmacia(int id, String nome, String endereco, String regiao, String horarioFuncionamento, String telefone) {
        super(id, nome, endereco, regiao, horarioFuncionamento, telefone);
    }

    @Override
    public String exibirHorario() {
        return "[Farmácia] Horário Comercial: " + super.getHorarioFuncionamento() + " (Sujeito à disponibilidade do farmacêutico responsável).";
    }
}