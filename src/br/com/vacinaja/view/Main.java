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
        try {
            System.out.println("\nRegistrar vacinação"); // [cite: 209]

            // 1. SELEÇÃO E VALIDAÇÃO DE USUÁRIO
            List<Usuario> usuarios = usuarioService.listarUsuarios();
            if (usuarios.isEmpty()) {
                System.out.println("Nenhum usuário cadastrado no sistema.");
                return;
            }
            
            System.out.println("Usuários cadastrados:"); // [cite: 210]
            for (int i = 0; i < usuarios.size(); i++) {
                System.out.println((i + 1) + "- " + usuarios.get(i).getNome() + " (" + usuarios.get(i).getIdade() + " anos)");
            }
            System.out.print("Escolha o usuário (número): "); // [cite: 211]
            int userIndex = Integer.parseInt(scanner.nextLine()) - 1;

            // VALIDAÇÃO DE ÍNDICE: Impede 'IndexOutOfBoundsException' se o usuário digitar um número fora da lista
            if (userIndex < 0 || userIndex >= usuarios.size()) {
                System.out.println("Erro: Usuário não encontrado na lista.");
                return;
            }
            Usuario usuario = usuarios.get(userIndex);

            // 2. SELEÇÃO E VALIDAÇÃO DE VACINA
            List<Vacina> vacinas = vacinaService.listarVacinas();
            if (vacinas.isEmpty()) {
                System.out.println("Nenhuma vacina cadastrada no sistema.");
                return;
            }
            
            System.out.println("\nVacinas disponíveis para registro:"); // [cite: 212]
            for (int i = 0; i < vacinas.size(); i++) {
                System.out.println((i + 1) + "- " + vacinas.get(i).getNome());
            }
            System.out.print("Escolha a vacina (número): "); // [cite: 216]
            int vacIndex = Integer.parseInt(scanner.nextLine()) - 1;

            if (vacIndex < 0 || vacIndex >= vacinas.size()) {
                System.out.println("Erro: Vacina não encontrada na lista.");
                return;
            }
            Vacina vacina = vacinas.get(vacIndex);

            // 3. SELEÇÃO E VALIDAÇÃO DE POSTO DE SAÚDE
            List<PostoSaude> postos = postoService.listarPostos();
            if (postos.isEmpty()) {
                System.out.println("Nenhum posto de saúde cadastrado no sistema.");
                return;
            }
            
            System.out.println("\nPostos de saúde disponíveis:");
            for (int i = 0; i < postos.size(); i++) {
                System.out.println((i + 1) + "- " + postos.get(i).getNome());
            }
            System.out.print("Escolha o posto onde tomou (número): "); 
            int postoIndex = Integer.parseInt(scanner.nextLine()) - 1;

            if (postoIndex < 0 || postoIndex >= postos.size()) {
                System.out.println("Erro: Posto de saúde não encontrado na lista.");
                return;
            }
            PostoSaude posto = postos.get(postoIndex);

            // 4. DADOS DA APLICAÇÃO (DATA E DOSE)
            System.out.print("\nData da vacinação (dd/mm/aaaa): "); // [cite: 217]
            String dataStr = scanner.nextLine();
            
            System.out.print("Dose (1/2/3/Única/Reforço): "); // [cite: 219]
            String dose = scanner.nextLine();
            
            // CONVERSÃO DE DATA E EFETIVAÇÃO DO REGISTRO
            // O sdf.parse pode lançar um ParseException se a pessoa digitar "15 de março" em vez de "15/03/2026"
            Date data = sdf.parse(dataStr); 
            usuarioService.registrarVacinacao(usuario, vacina, dose, data, posto); // [cite: 156, 162]
            
            System.out.println("Registro realizado com sucesso!"); // [cite: 220]

        // CONCEITO POO - TRATAMENTO DE EXCEÇÕES:
        // Captura diferentes tipos de falhas e exibe mensagens amigáveis em vez de crashar o console.
        } catch (NumberFormatException e) {
            System.out.println("Erro: Você deve digitar o NÚMERO correspondente na lista, não letras ou espaços.");
        } catch (ParseException e) {
            System.out.println("Erro: Formato de data inválido. Use exatamente o padrão dd/mm/aaaa (ex: 15/03/2026).");
        } catch (Exception e) {
            System.out.println("Erro inesperado ao registrar vacinação: " + e.getMessage());
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
        System.out.println("1- Cadastrar novo usuário (Nome e Idade)");
        System.out.println("2- Cadastrar novo usuário (Somente Nome - Demonstra Sobrecarga)");
        System.out.println("3- Listar usuários");
        System.out.print("Escolha: ");
        
        try {
            int opcao = Integer.parseInt(scanner.nextLine());
            int novoId = usuarioService.listarUsuarios().size() + 1;

            if (opcao == 1) {
                System.out.print("Nome: ");
                String nome = scanner.nextLine();
                System.out.print("Idade: ");
                int idade = Integer.parseInt(scanner.nextLine());
                
                // Tenta criar o usuário. Se a idade for negativa, o setter do Model lança um erro que é pego pelo catch.
                Usuario novoUser = new Usuario(novoId, nome, idade);
                usuarioService.cadastrarUsuario(novoUser);
                
            } else if (opcao == 2) {
                System.out.print("Nome: ");
                String nome = scanner.nextLine();
                
                // DEMONSTRAÇÃO DE SOBRECARGA: Chamando o construtor que não exige a idade.
                Usuario novoUser = new Usuario(novoId, nome);
                usuarioService.cadastrarUsuario(novoUser);
                System.out.println("Usuário cadastrado com a sobrecarga de construtor (Idade padrão: 0).");
                
            } else if (opcao == 3) {
                for (Usuario u : usuarioService.listarUsuarios()) {
                    System.out.println(u.toString());
                }
            } else {
                System.out.println("Opção inválida.");
            }
        // CONCEITO POO - TRATAMENTO DE EXCEÇÕES:
        // Se o IllegalArgumentException for acionado na classe Usuario, nós o capturamos aqui para avisar o operador.
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro: Entrada inválida. Certifique-se de digitar números onde solicitado.");
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