package br.com.vacinaja.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nome;
    
    private int idade;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RegistroVacinacao> listaRegistroVacinacao = new ArrayList<>();

    public Usuario() {}

    public Usuario(String nome, int idade) {
        this.setNome(nome);
        this.setIdade(idade);
    }

    public Usuario(String nome) {
        this.setNome(nome);
        this.idade = 0;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Erro: O nome do usuário não pode ficar em branco.");
        }
        this.nome = nome;
    }

    public int getIdade() { return idade; }
    public void setIdade(int idade) {
        if (idade < 0 || idade > 130) {
            throw new IllegalArgumentException("Erro: Idade inválida. Deve estar entre 0 e 130 anos.");
        }
        this.idade = idade;
    }

    public List<RegistroVacinacao> getListaRegistroVacinacao() { return listaRegistroVacinacao; }
    public void setListaRegistroVacinacao(List<RegistroVacinacao> listaRegistroVacinacao) {
        this.listaRegistroVacinacao = listaRegistroVacinacao;
    }

    public void adicionarRegistro(RegistroVacinacao registro) {
        if (registro != null) {
            this.listaRegistroVacinacao.add(registro);
            registro.setUsuario(this);
        }
    }

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", nome='" + nome + '\'' + ", idade=" + idade + '}';
    }
}