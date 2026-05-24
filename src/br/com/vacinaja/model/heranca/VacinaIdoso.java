package br.com.vacinaja.model.heranca;

import br.com.vacinaja.model.Vacina;
import java.util.Date;

public class VacinaIdoso extends Vacina {

    public VacinaIdoso(int id, String nome, String doencaPrevenida, int idadeMinima, int idadeMaxima, int dosesNecessarias, int intervaloDoses) {
        super(id, nome, doencaPrevenida, idadeMinima, idadeMaxima, dosesNecessarias, intervaloDoses);
    }

    @Override
    public Date calcularProximaDose(Date dataUltimaDose) {
        // Polimorfismo: Vacinas de idosos (como a da Gripe) frequentemente seguem campanhas anuais sazonais
        // Em vez de calcular uma data exata, utilizamos a base, mas emitimos um alerta de prioridade.
        System.out.println("[ALERTA] Paciente idoso: Verifique o calendário da próxima Campanha Nacional para " + this.getNome());
        return super.calcularProximaDose(dataUltimaDose); // Reutiliza a lógica em dias da classe mãe
    }
}