package br.com.vacinaja.model;

import jakarta.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
public class Vacina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nome;
    private String doencaPrevenida;
    private int idadeMinima;
    private int idadeMaxima;
    private int dosesNecessarias;
    private int intervaloDoses;

    public Vacina() {}

    public Vacina(String nome, String doencaPrevenida, int idadeMinima, int idadeMaxima, int dosesNecessarias, int intervaloDoses) {
        this.nome = nome;
        this.doencaPrevenida = doencaPrevenida;
        this.idadeMinima = idadeMinima;
        this.idadeMaxima = idadeMaxima;
        this.dosesNecessarias = dosesNecessarias;
        this.intervaloDoses = intervaloDoses;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDoencaPrevenida() { return doencaPrevenida; }
    public void setDoencaPrevenida(String doencaPrevenida) { this.doencaPrevenida = doencaPrevenida; }
    public int getIdadeMinima() { return idadeMinima; }
    public void setIdadeMinima(int idadeMinima) { this.idadeMinima = idadeMinima; }
    public int getIdadeMaxima() { return idadeMaxima; }
    public void setIdadeMaxima(int idadeMaxima) { this.idadeMaxima = idadeMaxima; }
    public int getDosesNecessarias() { return dosesNecessarias; }
    public void setDosesNecessarias(int dosesNecessarias) { this.dosesNecessarias = dosesNecessarias; }
    public int getIntervaloDoses() { return intervaloDoses; }
    public void setIntervaloDoses(int intervaloDoses) { this.intervaloDoses = intervaloDoses; }

    public Date calcularProximaDose(Date dataUltimaDose) {
        if (this.dosesNecessarias <= 1) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(dataUltimaDose);
        cal.add(Calendar.DAY_OF_MONTH, this.intervaloDoses);
        return cal.getTime();
    }
}