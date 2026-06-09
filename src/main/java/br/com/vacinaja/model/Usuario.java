package br.com.vacinaja.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome nao pode estar em branco")
    @Column(nullable = false)
    private String nome;

    @Min(value = 0, message = "A idade nao pode ser negativa")
    private int idade;

    @Email(message = "Informe um e-mail valido")
    @NotBlank(message = "O e-mail nao pode estar em branco")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "A senha nao pode estar em branco")
    @Column(nullable = false)
    private String senha;

    @NotBlank(message = "O perfil nao pode estar em branco")
    @Column(nullable = false)
    private String role = "USER";

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RegistroVacinacao> listaRegistroVacinacao = new ArrayList<>();

    public Usuario() {}

    public Usuario(String nome, int idade) {
        this.setNome(nome);
        this.setIdade(idade);
    }

    public Usuario(String nome, int idade, String email, String senha, String role) {
        this.setNome(nome);
        this.setIdade(idade);
        this.setEmail(email);
        this.setSenha(senha);
        this.setRole(role);
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
            throw new IllegalArgumentException("Erro: O nome do usuario nao pode ficar em branco.");
        }
        this.nome = nome;
    }

    public int getIdade() { return idade; }
    public void setIdade(int idade) {
        if (idade < 0 || idade > 130) {
            throw new IllegalArgumentException("Erro: Idade invalida. Deve estar entre 0 e 130 anos.");
        }
        this.idade = idade;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Erro: O e-mail do usuario nao pode ficar em branco.");
        }
        this.email = email.trim().toLowerCase();
    }

    public String getSenha() { return senha; }
    public void setSenha(String senha) {
        if (senha == null || senha.trim().isEmpty()) {
            throw new IllegalArgumentException("Erro: A senha do usuario nao pode ficar em branco.");
        }
        this.senha = senha;
    }

    public String getRole() { return role; }
    public void setRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            this.role = "USER";
            return;
        }
        String normalizedRole = role.trim().toUpperCase();
        this.role = "ADMIN".equals(normalizedRole) ? "ADMIN" : "USER";
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
        return "Usuario{" + "id=" + id + ", nome='" + nome + '\'' + ", idade=" + idade + ", email='" + email + '\'' + ", role='" + role + '\'' + '}';
    }
}
