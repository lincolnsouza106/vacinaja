package br.com.vacinaja.model.heranca;

import br.com.vacinaja.model.Vacina;
import java.util.Calendar;
import java.util.Date;

public class VacinaAdulto extends Vacina {

    public VacinaAdulto(int id, String nome, String doencaPrevenida, int idadeMinima, int idadeMaxima, int dosesNecessarias, int intervaloDoses) {
        super(id, nome, doencaPrevenida, idadeMinima, idadeMaxima, dosesNecessarias, intervaloDoses);
    }

    @Override
    public Date calcularProximaDose(Date dataUltimaDose) {
        if (this.getDosesNecessarias() <= 1) return null;

        // Polimorfismo: Para adultos, assumimos que o intervaloDoses representa ANOS (ex: reforço dT a cada 10 anos)
        Calendar cal = Calendar.getInstance();
        cal.setTime(dataUltimaDose);
        cal.add(Calendar.YEAR, this.getIntervaloDoses());
        
        return cal.getTime();
    }
}