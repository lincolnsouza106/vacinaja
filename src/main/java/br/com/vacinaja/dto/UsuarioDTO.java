package br.com.vacinaja.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class UsuarioDTO {

    @NotBlank(message = "O nome não pode estar em branco")
    private String nome;

    @Min(value = 0, message = "A idade não pode ser negativa")
    private int idade;

    public UsuarioDTO() {}

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public int getIdade() { return idade; }
    public void setIdade(int idade) { this.idade = idade; }
}
