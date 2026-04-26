package br.com.vacinaja.model;

import java.util.Date;

public class Campanha {
    private String nome;
    private String descricao;
    private Date dataInicio;
    private Date dataFim;
    private String publicoAlvo;

    public Campanha(String nome, String descricao, Date dataInicio, Date dataFim, String publicoAlvo) {
        this.nome = nome;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.publicoAlvo = publicoAlvo;
    }

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public Date getDataInicio() { return dataInicio; }
    public void setDataInicio(Date dataInicio) { this.dataInicio = dataInicio; }
    public Date getDataFim() { return dataFim; }
    public void setDataFim(Date dataFim) { this.dataFim = dataFim; }
    public String getPublicoAlvo() { return publicoAlvo; }
    public void setPublicoAlvo(String publicoAlvo) { this.publicoAlvo = publicoAlvo; }

    @Override
    public String toString() {
        return "Campanha{" + "nome='" + nome + '\'' + ", publicoAlvo='" + publicoAlvo + '\'' + '}';
    }
}