package br.com.vacinaja.model;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private int id;
    private String nome;
    private int idade;
    private List<RegistroVacinacao> listaRegistroVacinacao;

    public Usuario(int id, String nome, int idade) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.listaRegistroVacinacao = new ArrayList<>();
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public int getIdade() { return idade; }
    public void setIdade(int idade) { this.idade = idade; }
    public List<RegistroVacinacao> getListaRegistroVacinacao() { return listaRegistroVacinacao; }
    public void setListaRegistroVacinacao(List<RegistroVacinacao> listaRegistroVacinacao) { this.listaRegistroVacinacao = listaRegistroVacinacao; }

    public void adicionarRegistro(RegistroVacinacao registro) {
        this.listaRegistroVacinacao.add(registro);
    }

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", nome='" + nome + '\'' + ", idade=" + idade + '}';
    }
}