package br.com.vacinaja.model.heranca;

import br.com.vacinaja.model.PostoSaude;

public class ClinicaPrivada extends PostoSaude {

    public ClinicaPrivada(int id, String nome, String endereco, String regiao, String horarioFuncionamento, String telefone) {
        super(id, nome, endereco, regiao, horarioFuncionamento, telefone);
    }

    @Override
    public String exibirHorario() {
        return "[Clínica Privada] Atendimento com hora marcada. Horário: " + super.getHorarioFuncionamento();
    }
}