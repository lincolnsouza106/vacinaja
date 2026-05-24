package br.com.vacinaja.model;

import java.util.ArrayList;
import java.util.List;

public class PostoSaude {
    private int id;
    private String nome;
    private String endereco;
    private String regiao;
    private String horarioFuncionamento;
    private String telefone;
    private List<Vacina> vacinasDisponiveis;

    public PostoSaude(int id, String nome, String endereco, String regiao, String horarioFuncionamento, String telefone) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.regiao = regiao;
        this.horarioFuncionamento = horarioFuncionamento;
        this.telefone = telefone;
        this.vacinasDisponiveis = new ArrayList<>();
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
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

    public String exibirHorario() {
        return "Horário de Funcionamento: " + this.horarioFuncionamento;
    }

    // Método para associar uma vacina ao posto (Requisito 3)
    public void adicionarVacinaDisponivel(Vacina vacina) {
        if (!this.vacinasDisponiveis.contains(vacina)) {
            this.vacinasDisponiveis.add(vacina);
        }
    }

    @Override
    public String toString() {
        return "PostoSaude{" + "id=" + id + ", nome='" + nome + '\'' + ", regiao='" + regiao + '\'' + '}';
    }
}