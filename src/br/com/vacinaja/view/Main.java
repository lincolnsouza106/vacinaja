package br.com.vacinaja.view;

import br.com.vacinaja.model.*;
import br.com.vacinaja.model.heranca.*;
import br.com.vacinaja.service.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    // Instanciando os serviços globalmente para a View
    private static UsuarioService usuarioService = new UsuarioService();
    private static VacinaService vacinaService = new VacinaService();
    private static PostoService postoService = new PostoService();
    private static CampanhaService campanhaService = new CampanhaService();
    
    private static Scanner scanner = new Scanner(System.in);
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public static void main(String[] args) {
        inicializarDadosMock(); // Preenche o sistema com dados iniciais para teste

        int opcao = 0;
        while (opcao != 7) {
            System.out.println("\n=== VACINA JÁ ==="); 
            System.out.println("1- Consultar vacinas por idade"); 
            System.out.println("2- Localizar posto de saúde"); 
            System.out.println("3- Registrar vacinação");
            System.out.println("4- Verificar pendências"); 
            System.out.println("5- Campanhas atuais"); 
            System.out.println("6- Gerenciar usuários"); 
            System.out.println("7- Sair"); 
            System.out.print("Escolha: "); 
            
            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                opcao = 0;
            }

            switch (opcao) {
                case 1:
                    consultarVacinasPorIdade();
                    break;
                case 2:
                    localizarPosto();
                    break;
                case 3:
                    registrarVacinacao();
                    break;
                case 4:
                    verificarPendencias();
                    break;
                case 5:
                    listarCampanhas();
                    break;
                case 6:
                    gerenciarUsuarios();
                    break;
                case 7:
                    System.out.println("Encerrando o sistema VacinaJá. Até logo!");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        }
        scanner.close();
    }

    private static void consultarVacinasPorIdade() {
        System.out.print("Informe sua idade: "); 
        int idade = Integer.parseInt(scanner.nextLine());
        System.out.println("\nVACINAS RECOMENDADAS PARA " + idade + " ANOS:"); 
        
        List<Vacina> recomendadas = vacinaService.consultarPorIdade(idade);
        if (recomendadas.isEmpty()) {
            System.out.println("Nenhuma vacina encontrada para esta idade.");
        } else {
            for (Vacina v : recomendadas) {
                System.out.println("- " + v.getNome() + " (Previne: " + v.getDoencaPrevenida() + ")");
            }
        }
    }

    private static void localizarPosto() {
        System.out.println("\nLocalizar posto de saúde"); 
        System.out.print("Região: "); 
        String regiao = scanner.nextLine();
        System.out.print("Vacina desejada (deixe em branco para todas): "); 
        String vacinaDesejada = scanner.nextLine();

        List<PostoSaude> encontrados = postoService.buscarPorRegiaoEVacina(regiao, vacinaDesejada);
        
        System.out.println("\nPOSTOS ENCONTRADOS:"); 
        for (PostoSaude p : encontrados) {
            System.out.println(p.getNome()); 
            System.out.println("Endereço: " + p.getEndereco()); 
            System.out.println(p.exibirHorario()); // Demonstração do Polimorfismo 
            System.out.println("Telefone: " + p.getTelefone()); 
            System.out.println("-------------------------");
        }
    }

    private static void registrarVacinacao() {
        System.out.println("\nRegistrar vacinação"); 
        System.out.println("Usuários cadastrados:"); 
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        for (int i = 0; i < usuarios.size(); i++) {
            System.out.println((i + 1) + "- " + usuarios.get(i).getNome() + " (" + usuarios.get(i).getIdade() + " anos)"); 
        }
        System.out.print("Escolha o usuário (número): "); 
        int userIndex = Integer.parseInt(scanner.nextLine()) - 1;
        Usuario usuario = usuarios.get(userIndex);

        System.out.println("\nVacinas disponíveis no sistema:"); 
        List<Vacina> vacinas = vacinaService.listarVacinas();
        for (int i = 0; i < vacinas.size(); i++) {
            System.out.println((i + 1) + "- " + vacinas.get(i).getNome()); 
        }
        System.out.print("Escolha a vacina (número): "); 
        int vacIndex = Integer.parseInt(scanner.nextLine()) - 1;
        Vacina vacina = vacinas.get(vacIndex);

        System.out.print("Data da vacinação (dd/mm/aaaa): "); 
        String dataStr = scanner.nextLine();
        System.out.print("Dose (1/2/3/Única/Reforço): "); 
        String dose = scanner.nextLine();
        
        try {
            Date data = sdf.parse(dataStr);
            // Assumindo o primeiro posto para simplificar o exemplo prático
            PostoSaude posto = postoService.listarPostos().get(0); 
            usuarioService.registrarVacinacao(usuario, vacina, dose, data, posto);
            System.out.println("Registro realizado com sucesso!"); 
        } catch (ParseException e) {
            System.out.println("Erro formato de data inválido.");
        }
    }

    private static void verificarPendencias() {
        System.out.println("\nVerificar pendências"); 
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        System.out.print("ID do usuário para verificar (1 a " + usuarios.size() + "): ");
        int userIndex = Integer.parseInt(scanner.nextLine()) - 1;
        Usuario usuario = usuarios.get(userIndex);

        System.out.println("\nVACINAS EM ATRASO PARA " + usuario.getNome().toUpperCase() + ":"); 
        List<Vacina> pendentes = usuarioService.verificarPendencias(usuario, vacinaService.listarVacinas());
        
        if (pendentes.isEmpty()) {
            System.out.println("Carteira de vacinação em dia!");
        } else {
            for (Vacina v : pendentes) {
                System.out.println("- " + v.getNome() + " (Faltam doses. Requeridas: " + v.getDosesNecessarias() + ")");
            }
        }
    }

    private static void listarCampanhas() {
        System.out.println("\nCAMPANHAS ATUAIS"); 
        for (Campanha c : campanhaService.listarCampanhas()) {
            System.out.println("- " + c.getNome() + " | Público: " + c.getPublicoAlvo()); 
        }
    }

    private static void gerenciarUsuarios() {
        System.out.println("\nGerenciar usuários"); 
        System.out.println("1- Cadastrar novo usuário"); 
        System.out.println("2- Listar usuários"); 
        System.out.print("Escolha: "); 
        int opcao = Integer.parseInt(scanner.nextLine());

        if (opcao == 1) {
            System.out.print("Nome: "); 
            String nome = scanner.nextLine();
            System.out.print("Idade: "); 
            int idade = Integer.parseInt(scanner.nextLine());
            
            int novoId = usuarioService.listarUsuarios().size() + 1;
            Usuario novoUser = new Usuario(novoId, nome, idade);
            usuarioService.cadastrarUsuario(novoUser);
            System.out.println("Usuário cadastrado com sucesso! ID: " + novoId); 
        } else if (opcao == 2) {
            for (Usuario u : usuarioService.listarUsuarios()) {
                System.out.println(u.toString());
            }
        }
    }

    // Método para popular o sistema inicialmente
    private static void inicializarDadosMock() {
        // Mock Usuários
        usuarioService.cadastrarUsuario(new Usuario(1, "João Silva", 25)); 
        usuarioService.cadastrarUsuario(new Usuario(2, "Maria Santos", 68)); 

        // Mock Vacinas
        Vacina febreAmarela = new VacinaAdulto(1, "Febre Amarela", "Febre Amarela", 0, 100, 1, 3650); 
        Vacina gripe = new VacinaIdoso(2, "Gripe", "Influenza", 0, 100, 1, 365);
        vacinaService.cadastrarVacina(febreAmarela);
        vacinaService.cadastrarVacina(gripe);

        // Mock Postos
        UBS ubs1 = new UBS(1, "UBS Jardim América", "Av. das Nações, 789", "Zona Sul", "Seg-Sex 7h-19h", "(11) 5555-1234"); 
        ubs1.adicionarVacinaDisponivel(febreAmarela); 
        ubs1.adicionarVacinaDisponivel(gripe); 
        postoService.cadastrarPosto(ubs1);

        // Mock Campanhas
        try {
            Campanha camp = new Campanha("Carnaval Seguro", "Reforço contra Febre Amarela", sdf.parse("01/02/2026"), sdf.parse("28/03/2026"), "Viajantes"); 
            campanhaService.cadastrarCampanha(camp);
        } catch (ParseException e) { }
    }
}