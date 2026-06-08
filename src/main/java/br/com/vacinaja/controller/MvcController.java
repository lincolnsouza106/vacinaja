package br.com.vacinaja.controller;

import br.com.vacinaja.model.*;
import br.com.vacinaja.service.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

@Controller
public class MvcController {

    private final UsuarioService usuarioService;
    private final VacinaService vacinaService;
    private final PostoService postoService;
    private final CampanhaService campanhaService;

    public MvcController(UsuarioService usuarioService, VacinaService vacinaService, PostoService postoService, CampanhaService campanhaService) {
        this.usuarioService = usuarioService;
        this.vacinaService = vacinaService;
        this.postoService = postoService;
        this.campanhaService = campanhaService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String vacinas(Model model, @RequestParam(required = false) Integer idade) {
        model.addAttribute("view", "vacinas");
        model.addAttribute("idade", idade);
        if (idade != null) {
            model.addAttribute("vacinas", vacinaService.consultarPorIdade(idade));
        }
        return "index";
    }

    @GetMapping("/postos")
    public String postos(Model model, @RequestParam(required = false) String regiao, @RequestParam(required = false) String vacinaNome) {
        model.addAttribute("view", "postos");
        model.addAttribute("regiao", regiao);
        model.addAttribute("vacinaNome", vacinaNome);
        if (regiao != null || vacinaNome != null) {
            model.addAttribute("postos", postoService.buscarPorRegiaoEVacina(regiao, vacinaNome));
            model.addAttribute("buscou", true);
        }
        return "index";
    }

    @GetMapping("/registrar")
    public String registrar(Model model) {
        model.addAttribute("view", "registrar");
        model.addAttribute("usuarios", usuarioService.listarUsuarios());
        model.addAttribute("vacinas", vacinaService.listarVacinas());
        model.addAttribute("postos", postoService.listarPostos());
        return "index";
    }

    @PostMapping("/registrar")
    public String salvarRegistro(@RequestParam Long usuarioId, 
                                 @RequestParam Long vacinaId, 
                                 @RequestParam Long postoId,
                                 @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date data,
                                 @RequestParam String dose,
                                 RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = usuarioService.buscarPorId(usuarioId);
            Vacina vacina = vacinaService.listarVacinas().stream().filter(v -> v.getId().equals(vacinaId)).findFirst().orElseThrow();
            PostoSaude posto = postoService.listarPostos().stream().filter(p -> p.getId().equals(postoId)).findFirst().orElseThrow();

            RegistroVacinacao registro = new RegistroVacinacao(dose, data, vacina, posto);
            registro.setUsuario(usuario);
            usuario.adicionarRegistro(registro);
            usuarioService.cadastrarUsuario(usuario); // Save updates to user list of records
            
            redirectAttributes.addFlashAttribute("mensagem", "Vacinação registrada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagem", "Erro ao registrar vacinação.");
        }
        return "redirect:/registrar";
    }

    @GetMapping("/pendencias")
    public String pendencias(Model model, @RequestParam(required = false) Long usuarioId) {
        model.addAttribute("view", "pendencias");
        model.addAttribute("usuarios", usuarioService.listarUsuarios());
        if (usuarioId != null) {
            model.addAttribute("usuarioId", usuarioId);
            model.addAttribute("pendentes", usuarioService.verificarPendencias(usuarioId));
            model.addAttribute("buscou", true);
        }
        return "index";
    }

    @GetMapping("/campanhas")
    public String campanhas(Model model) {
        model.addAttribute("view", "campanhas");
        model.addAttribute("campanhas", campanhaService.listarCampanhas());
        return "index";
    }

    @GetMapping("/usuarios")
    public String usuarios(Model model) {
        model.addAttribute("view", "usuarios");
        model.addAttribute("usuarios", usuarioService.listarUsuarios());
        return "index";
    }

    @PostMapping("/usuarios")
    public String cadastrarUsuario(@jakarta.validation.Valid @ModelAttribute br.com.vacinaja.dto.UsuarioDTO dto, org.springframework.validation.BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("erroValidacao", result.getAllErrors().get(0).getDefaultMessage());
            return "redirect:/usuarios";
        }
        Usuario novo = new Usuario(dto.getNome(), dto.getIdade());
        usuarioService.cadastrarUsuario(novo);
        redirectAttributes.addFlashAttribute("mensagem", "Usuário cadastrado com sucesso!");
        return "redirect:/usuarios";
    }
}
