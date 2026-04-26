package br.com.vacinaja.view;

import br.com.vacinaja.model.Usuario;
import br.com.vacinaja.model.Vacina;
import br.com.vacinaja.service.UsuarioService;
import br.com.vacinaja.service.VacinaService;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Sistema VacinaJá Iniciado ===");

        // Instanciando Serviços
        UsuarioService usuarioService = new UsuarioService();
        VacinaService vacinaService = new VacinaService();

        // Criando Objetos (Model)
        Usuario u1 = new Usuario(1, "Lincoln Bezerra", 25);
        Usuario u2 = new Usuario(2, "Michael Coutinho", 30);
        
        Vacina v1 = new Vacina(1, "Vacina da Gripe", "Influenza", 0, 100, 1, 365);

        // Testando os métodos, ArrayLists e Serviços
        usuarioService.cadastrarUsuario(u1);
        usuarioService.cadastrarUsuario(u2);
        vacinaService.cadastrarVacina(v1);

        System.out.println("\n--- Lista de Usuários ---");
        for(Usuario u : usuarioService.listarUsuarios()) {
            // Demonstra o uso do toString() sobrescrito
            System.out.println(u.toString()); 
        }
    }
}