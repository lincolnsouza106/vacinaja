package br.com.vacinaja.model.heranca;

import br.com.vacinaja.model.Vacina;
import java.util.Calendar;
import java.util.Date;

public class VacinaInfantil extends Vacina {

    public VacinaInfantil(int id, String nome, String doencaPrevenida, int idadeMinima, int idadeMaxima, int dosesNecessarias, int intervaloDoses) {
        super(id, nome, doencaPrevenida, idadeMinima, idadeMaxima, dosesNecessarias, intervaloDoses);
    }

    @Override
    public Date calcularProximaDose(Date dataUltimaDose) {
        if (this.getDosesNecessarias() <= 1) return null;
        
        // Polimorfismo: Para vacinas infantis, assumimos que o intervaloDoses é em MESES (ex: vacina aos 2, 4 e 6 meses)
        Calendar cal = Calendar.getInstance();
        cal.setTime(dataUltimaDose);
        cal.add(Calendar.MONTH, this.getIntervaloDoses());
        
        System.out.println("Atenção Responsável: Anote no cartão da criança a próxima dose de " + this.getNome());
        return cal.getTime();
    }
}