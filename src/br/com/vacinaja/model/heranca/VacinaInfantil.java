package br.com.vacinaja.model.heranca;

import br.com.vacinaja.model.Vacina;

public class VacinaInfantil extends Vacina {
    public VacinaInfantil(int id, String nome, String doencaPrevenida, int idadeMinima, int idadeMaxima, int dosesNecessarias, int intervaloDoses) {
        super(id, nome, doencaPrevenida, idadeMinima, idadeMaxima, dosesNecessarias, intervaloDoses);
    }
}