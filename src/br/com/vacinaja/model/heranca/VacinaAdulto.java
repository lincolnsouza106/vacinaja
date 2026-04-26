package br.com.vacinaja.model.heranca;

import br.com.vacinaja.model.Vacina;

public class VacinaAdulto extends Vacina {
    public VacinaAdulto(int id, String nome, String doencaPrevenida, int idadeMinima, int idadeMaxima, int dosesNecessarias, int intervaloDoses) {
        super(id, nome, doencaPrevenida, idadeMinima, idadeMaxima, dosesNecessarias, intervaloDoses);
    }
}