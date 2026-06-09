package br.com.vacinaja.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsuarioDTO {

    @NotBlank(message = "O nome nao pode estar em branco")
    private String nome;

    @Min(value = 0, message = "A idade nao pode ser negativa")
    private int idade;

    @Email(message = "Informe um e-mail valido")
    @NotBlank(message = "O e-mail nao pode estar em branco")
    private String email;

    @NotBlank(message = "A senha nao pode estar em branco")
    @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
    private String senha;

    public UsuarioDTO() {}

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public int getIdade() { return idade; }
    public void setIdade(int idade) { this.idade = idade; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}
