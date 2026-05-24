package br.com.vacinaja.model.heranca;

import br.com.vacinaja.model.PostoSaude;

public class UBS extends PostoSaude {

    public UBS(int id, String nome, String endereco, String regiao, String horarioFuncionamento, String telefone) {
        super(id, nome, endereco, regiao, horarioFuncionamento, telefone);
    }

    @Override
    public String exibirHorario() {
        return "[UBS - Rede Pública] Horário de Funcionamento: " + super.getHorarioFuncionamento() + " (Distribuição de senhas pode ocorrer mais cedo).";
    }
}