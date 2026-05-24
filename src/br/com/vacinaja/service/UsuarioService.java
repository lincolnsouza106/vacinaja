package br.com.vacinaja.service;

import br.com.vacinaja.model.RegistroVacinacao;
import br.com.vacinaja.model.Usuario;
import br.com.vacinaja.model.Vacina;
import br.com.vacinaja.model.PostoSaude;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UsuarioService {
    private List<Usuario> usuarios;

    public UsuarioService() {
        this.usuarios = new ArrayList<>();
    }

    public void cadastrarUsuario(Usuario usuario) {
        this.usuarios.add(usuario);
        System.out.println("Usuário cadastrado: " + usuario.getNome());
    }

    public List<Usuario> listarUsuarios() {
        return this.usuarios;
    }

    // Requisito 6: Registrar que o usuário tomou a dose
    public void registrarVacinacao(Usuario usuario, Vacina vacina, String dose, Date data, PostoSaude posto) {
        RegistroVacinacao registro = new RegistroVacinacao(dose, data, vacina, posto);
        usuario.adicionarRegistro(registro);
        System.out.println("Vacinação registrada com sucesso para " + usuario.getNome() + "!");
    }

    // Requisito 7: Verificar pendências comparando a idade do usuário com todas as vacinas do sistema
    public List<Vacina> verificarPendencias(Usuario usuario, List<Vacina> todasAsVacinasDoSistema) {
        List<Vacina> pendentes = new ArrayList<>();

        for (Vacina vacina : todasAsVacinasDoSistema) {
            // Passo 1: Verifica se a vacina é recomendada para a idade do usuário
            if (usuario.getIdade() >= vacina.getIdadeMinima() && usuario.getIdade() <= vacina.getIdadeMaxima()) {
                
                // Passo 2: Conta quantas doses dessa vacina o usuário já tomou no histórico
                int dosesTomadas = 0;
                for (RegistroVacinacao registro : usuario.getListaRegistroVacinacao()) {
                    if (registro.getVacina().getId() == vacina.getId()) {
                        dosesTomadas++;
                    }
                }

                // Passo 3: Se ele tomou menos doses do que o necessário, está pendente
                if (dosesTomadas < vacina.getDosesNecessarias()) {
                    pendentes.add(vacina);
                }
            }
        }
        return pendentes;
    }
}