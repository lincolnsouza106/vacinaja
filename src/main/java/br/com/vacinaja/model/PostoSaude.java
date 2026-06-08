package br.com.vacinaja.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class PostoSaude {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nome;
    private String endereco;
    private String regiao;
    private String horarioFuncionamento;
    private String telefone;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "posto_vacina",
        joinColumns = @JoinColumn(name = "posto_id"),
        inverseJoinColumns = @JoinColumn(name = "vacina_id")
    )
    private List<Vacina> vacinasDisponiveis = new ArrayList<>();

    public PostoSaude() {}

    public PostoSaude(String nome, String endereco, String regiao, String horarioFuncionamento, String telefone) {
        this.nome = nome;
        this.endereco = endereco;
        this.regiao = regiao;
        this.horarioFuncionamento = horarioFuncionamento;
        this.telefone = telefone;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getRegiao() { return regiao; }
    public void setRegiao(String regiao) { this.regiao = regiao; }
    public String getHorarioFuncionamento() { return horarioFuncionamento; }
    public void setHorarioFuncionamento(String horarioFuncionamento) { this.horarioFuncionamento = horarioFuncionamento; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public List<Vacina> getVacinasDisponiveis() {
        return vacinasDisponiveis;
    }

    public void setVacinasDisponiveis(List<Vacina> vacinasDisponiveis) {
        this.vacinasDisponiveis = vacinasDisponiveis;
    }

    public void adicionarVacinaDisponivel(Vacina vacina) {
        if (!this.vacinasDisponiveis.contains(vacina)) {
            this.vacinasDisponiveis.add(vacina);
        }
    }
}