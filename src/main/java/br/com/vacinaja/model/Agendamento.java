package br.com.vacinaja.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Agendamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "vacina_id", nullable = false)
    private Vacina vacina;

    @ManyToOne
    @JoinColumn(name = "posto_saude_id", nullable = false)
    private PostoSaude postoSaude;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private LocalTime horario;

    @Column(nullable = false)
    private String status = "AGENDADO";

    private String observacao;

    public Agendamento() {}

    public Agendamento(Usuario usuario, Vacina vacina, PostoSaude postoSaude, LocalDate data, LocalTime horario, String observacao) {
        this.usuario = usuario;
        this.vacina = vacina;
        this.postoSaude = postoSaude;
        this.data = data;
        this.horario = horario;
        this.observacao = observacao;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Vacina getVacina() { return vacina; }
    public void setVacina(Vacina vacina) { this.vacina = vacina; }

    public PostoSaude getPostoSaude() { return postoSaude; }
    public void setPostoSaude(PostoSaude postoSaude) { this.postoSaude = postoSaude; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public LocalTime getHorario() { return horario; }
    public void setHorario(LocalTime horario) { this.horario = horario; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
}
