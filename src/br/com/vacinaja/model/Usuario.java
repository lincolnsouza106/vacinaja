package br.com.vacinaja.model;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private int id;
    private String nome;
    private int idade;
    private List<RegistroVacinacao> listaRegistroVacinacao;

    // CONCEITO POO - CONSTRUTOR PRINCIPAL: Inicializa o objeto com todos os atributos básicos.
    public Usuario(int id, String nome, int idade) {
        this.id = id;
        this.setNome(nome);   // Usamos os setters no construtor para aproveitar a validação!
        this.setIdade(idade); 
        this.listaRegistroVacinacao = new ArrayList<>();
    }

    // CONCEITO POO - SOBRECARGA DE CONSTRUTORES (Overloading): 
    // Uma segunda opção para criar o objeto caso a idade não seja fornecida de imediato.
    public Usuario(int id, String nome) {
        this.id = id;
        this.setNome(nome);
        this.idade = 0; // Valor padrão até que seja atualizado
        this.listaRegistroVacinacao = new ArrayList<>();
    }

    // Getters
    public int getId() { return id; }
    public String getNome() { return nome; }
    public int getIdade() { return idade; }
    public List<RegistroVacinacao> getListaRegistroVacinacao() { return listaRegistroVacinacao; }

    // CONCEITO POO - ENCAPSULAMENTO COM VALIDAÇÃO: 
    // O atributo 'nome' é privado. Só pode ser alterado por aqui, o que nos permite bloquear nomes vazios.
    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Erro: O nome do usuário não pode ficar em branco.");
        }
        this.nome = nome;
    }

    // CONCEITO POO - ENCAPSULAMENTO COM VALIDAÇÃO: 
    // Impede que o sistema aceite uma idade negativa, protegendo o estado do objeto.
    public void setIdade(int idade) {
        if (idade < 0 || idade > 130) {
            throw new IllegalArgumentException("Erro: Idade inválida. Deve estar entre 0 e 130 anos.");
        }
        this.idade = idade;
    }

    public void adicionarRegistro(RegistroVacinacao registro) {
        if (registro != null) {
            this.listaRegistroVacinacao.add(registro);
        }
    }

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", nome='" + nome + '\'' + ", idade=" + idade + '}';
    }
}