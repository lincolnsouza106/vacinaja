package br.com.vacinaja.model;

public class Vacina {
    private int id;
    private String nome;
    private String doencaPrevenida;
    private int idadeMinima;
    private int idadeMaxima;
    private int dosesNecessarias;
    private int intervaloDoses;

    public Vacina(int id, String nome, String doencaPrevenida, int idadeMinima, int idadeMaxima, int dosesNecessarias, int intervaloDoses) {
        this.id = id;
        this.nome = nome;
        this.doencaPrevenida = doencaPrevenida;
        this.idadeMinima = idadeMinima;
        this.idadeMaxima = idadeMaxima;
        this.dosesNecessarias = dosesNecessarias;
        this.intervaloDoses = intervaloDoses;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
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

    @Override
    public String toString() {
        return "Vacina{" + "id=" + id + ", nome='" + nome + '\'' + ", doencaPrevenida='" + doencaPrevenida + '\'' + '}';
    }
}