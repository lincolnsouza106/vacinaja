package br.com.vacinaja.model;

import jakarta.persistence.*;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class RegistroVacinacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dose;
    
    @Temporal(TemporalType.DATE)
    private Date data;

    @ManyToOne
    @JoinColumn(name = "vacina_id")
    private Vacina vacina;

    @ManyToOne
    @JoinColumn(name = "posto_saude_id")
    private PostoSaude postoSaude;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    @JsonIgnore
    private Usuario usuario;

    public RegistroVacinacao() {}

    public RegistroVacinacao(String dose, Date data, Vacina vacina, PostoSaude postoSaude) {
        this.dose = dose;
        this.data = data;
        this.vacina = vacina;
        this.postoSaude = postoSaude;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getDose() { return dose; }
    public void setDose(String dose) { this.dose = dose; }
    
    public Date getData() { return data; }
    public void setData(Date data) { this.data = data; }
    
    public Vacina getVacina() { return vacina; }
    public void setVacina(Vacina vacina) { this.vacina = vacina; }
    
    public PostoSaude getPostoSaude() { return postoSaude; }
    public void setPostoSaude(PostoSaude postoSaude) { this.postoSaude = postoSaude; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
