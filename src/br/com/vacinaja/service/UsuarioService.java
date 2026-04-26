package br.com.vacinaja.service;

import br.com.vacinaja.model.Usuario;
import java.util.ArrayList;
import java.util.List;

public class UsuarioService {
    // Lista declarada no serviço como repositório em memória
    private List<Usuario> usuarios;

    public UsuarioService() {
        this.usuarios = new ArrayList<>();
    }

    public void cadastrarUsuario(Usuario usuario) {
        this.usuarios.add(usuario);
        System.out.println("Usuário cadastrado com sucesso: " + usuario.getNome());
    }

    public List<Usuario> listarUsuarios() {
        return this.usuarios;
    }

    public void verificarPendencias(Usuario usuario) {
        // Lógica futura para cruzar idade do usuário com calendário vacinal
        System.out.println("Verificando pendências para o usuário: " + usuario.getNome());
    }
}