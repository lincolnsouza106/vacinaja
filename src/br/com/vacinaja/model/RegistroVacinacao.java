package br.com.vacinaja.model;

import java.util.Date;

public class RegistroVacinacao {
    private String dose;
    private Date data;
    private Vacina vacina; // Relação de associação 
    private PostoSaude postoSaude; // Relação de associação 

    public RegistroVacinacao(String dose, Date data, Vacina vacina, PostoSaude postoSaude) {
        this.dose = dose;
        this.data = data;
        this.vacina = vacina;
        this.postoSaude = postoSaude;
    }

    // Getters e Setters
    public String getDose() { return dose; }
    public void setDose(String dose) { this.dose = dose; }
    public Date getData() { return data; }
    public void setData(Date data) { this.data = data; }
    public Vacina getVacina() { return vacina; }
    public void setVacina(Vacina vacina) { this.vacina = vacina; }
    public PostoSaude getPostoSaude() { return postoSaude; }
    public void setPostoSaude(PostoSaude postoSaude) { this.postoSaude = postoSaude; }

    @Override
    public String toString() {
        return "RegistroVacinacao{" + "dose='" + dose + '\'' + ", data=" + data + ", vacina=" + vacina.getNome() + ", local=" + postoSaude.getNome() + '}';
    }
}