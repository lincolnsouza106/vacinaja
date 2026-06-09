package br.com.vacinaja.controller;

import br.com.vacinaja.dto.UsuarioDTO;
import br.com.vacinaja.model.Agendamento;
import br.com.vacinaja.model.Campanha;
import br.com.vacinaja.model.PostoSaude;
import br.com.vacinaja.model.RegistroVacinacao;
import br.com.vacinaja.model.Usuario;
import br.com.vacinaja.model.Vacina;
import br.com.vacinaja.service.AgendamentoService;
import br.com.vacinaja.service.CampanhaService;
import br.com.vacinaja.service.PostoService;
import br.com.vacinaja.service.UsuarioService;
import br.com.vacinaja.service.VacinaService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
public class MvcController {

    private final UsuarioService usuarioService;
    private final VacinaService vacinaService;
    private final PostoService postoService;
    private final CampanhaService campanhaService;
    private final AgendamentoService agendamentoService;
    private final PasswordEncoder passwordEncoder;

    public MvcController(UsuarioService usuarioService,
                         VacinaService vacinaService,
                         PostoService postoService,
                         CampanhaService campanhaService,
                         AgendamentoService agendamentoService,
                         PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.vacinaService = vacinaService;
        this.postoService = postoService;
        this.campanhaService = campanhaService;
        this.agendamentoService = agendamentoService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String vacinas(Model model, @RequestParam(required = false) Integer idade, Authentication authentication) {
        model.addAttribute("view", "vacinas");
        model.addAttribute("idade", idade);
        preencherCarteira(model, authentication);
        return "index";
    }

    @GetMapping("/carteira")
    public String carteira(Model model, Authentication authentication) {
        model.addAttribute("view", "carteira");
        preencherCarteira(model, authentication);
        return "index";
    }

    @GetMapping("/postos")
    public String postos(Model model, @RequestParam(required = false) String regiao, @RequestParam(required = false) String vacinaNome) {
        model.addAttribute("view", "postos");
        model.addAttribute("regiao", regiao);
        model.addAttribute("vacinaNome", vacinaNome);
        model.addAttribute("vacinas", vacinaService.listarVacinas());
        model.addAttribute("todosPostos", postoService.listarPostos());
        if (regiao != null || vacinaNome != null) {
            model.addAttribute("postos", postoService.buscarPorRegiaoEVacina(regiao, vacinaNome));
            model.addAttribute("buscou", true);
        }
        return "index";
    }

    @PostMapping("/admin/postos")
    public String salvarPosto(@RequestParam(required = false) Long id,
                              @RequestParam String nome,
                              @RequestParam String endereco,
                              @RequestParam String regiao,
                              @RequestParam String horarioFuncionamento,
                              @RequestParam String telefone,
                              @RequestParam(required = false) List<Long> vacinaIds,
                              RedirectAttributes redirectAttributes) {
        PostoSaude posto = id != null ? postoService.buscarPorId(id) : new PostoSaude();
        posto.setNome(nome);
        posto.setEndereco(endereco);
        posto.setRegiao(regiao);
        posto.setHorarioFuncionamento(horarioFuncionamento);
        posto.setTelefone(telefone);
        posto.setVacinasDisponiveis(buscarVacinas(vacinaIds));
        postoService.cadastrarPosto(posto);
        redirectAttributes.addFlashAttribute("mensagem", "Posto salvo com sucesso!");
        return "redirect:/postos";
    }

    @PostMapping("/admin/postos/{id}/excluir")
    public String excluirPosto(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            postoService.excluirPosto(id);
            redirectAttributes.addFlashAttribute("mensagem", "Posto excluido com sucesso!");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("erroValidacao", "Este posto possui registros vinculados e nao pode ser excluido.");
        }
        return "redirect:/postos";
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
            Vacina vacina = vacinaService.buscarPorId(vacinaId);
            PostoSaude posto = postoService.buscarPorId(postoId);

            RegistroVacinacao registro = new RegistroVacinacao(dose, data, vacina, posto);
            registro.setUsuario(usuario);
            usuario.adicionarRegistro(registro);
            usuarioService.cadastrarUsuario(usuario);

            redirectAttributes.addFlashAttribute("mensagem", "Vacinacao registrada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagem", "Erro ao registrar vacinacao.");
        }
        return "redirect:/registrar";
    }

    @GetMapping("/agendamentos")
    public String agendamentos(Model model, Authentication authentication) {
        model.addAttribute("view", "agendamentos");
        model.addAttribute("admin", isAdmin(authentication));
        model.addAttribute("vacinas", vacinaService.listarVacinas());
        model.addAttribute("postos", postoService.listarPostos());

        if (isAdmin(authentication)) {
            model.addAttribute("usuarios", usuarioService.listarUsuarios());
            model.addAttribute("agendamentos", agendamentoService.listarTodos());
            return "index";
        }

        Usuario usuario = usuarioService.buscarPorEmail(authentication.getName());
        model.addAttribute("usuarioAtual", usuario);
        model.addAttribute("agendamentos", agendamentoService.listarPorUsuario(usuario));
        return "index";
    }

    @PostMapping("/agendamentos")
    public String salvarAgendamento(@RequestParam(required = false) Long usuarioId,
                                    @RequestParam Long vacinaId,
                                    @RequestParam Long postoId,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime horario,
                                    @RequestParam(required = false) String observacao,
                                    Authentication authentication,
                                    RedirectAttributes redirectAttributes) {
        Usuario usuario = isAdmin(authentication) && usuarioId != null
            ? usuarioService.buscarPorId(usuarioId)
            : usuarioService.buscarPorEmail(authentication.getName());
        agendamentoService.agendar(usuario, vacinaService.buscarPorId(vacinaId), postoService.buscarPorId(postoId), data, horario, observacao);
        redirectAttributes.addFlashAttribute("mensagem", "Agendamento criado com sucesso!");
        return "redirect:/agendamentos";
    }

    @PostMapping("/agendamentos/{id}/cancelar")
    public String cancelarAgendamento(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        if (!isAdmin(authentication)) {
            Usuario usuario = usuarioService.buscarPorEmail(authentication.getName());
            if (!agendamentoService.buscarPorId(id).getUsuario().getId().equals(usuario.getId())) {
                redirectAttributes.addFlashAttribute("erroValidacao", "Voce nao pode cancelar este agendamento.");
                return "redirect:/agendamentos";
            }
        }
        agendamentoService.cancelar(id);
        redirectAttributes.addFlashAttribute("mensagem", "Agendamento cancelado.");
        return "redirect:/agendamentos";
    }

    @GetMapping("/pendencias")
    public String pendencias(Model model, @RequestParam(required = false) Long usuarioId, Authentication authentication) {
        model.addAttribute("view", "pendencias");
        boolean admin = isAdmin(authentication);
        model.addAttribute("admin", admin);

        if (!admin && authentication != null) {
            Usuario usuario = usuarioService.buscarPorEmail(authentication.getName());
            model.addAttribute("usuarioAtual", usuario);
            model.addAttribute("usuarioId", usuario.getId());
            model.addAttribute("pendentes", usuarioService.verificarPendencias(usuario.getId()));
            model.addAttribute("buscou", true);
            return "index";
        }

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

    @PostMapping("/admin/campanhas")
    public String salvarCampanha(@RequestParam(required = false) Long id,
                                 @RequestParam String nome,
                                 @RequestParam String descricao,
                                 @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dataInicio,
                                 @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dataFim,
                                 @RequestParam String publicoAlvo,
                                 RedirectAttributes redirectAttributes) {
        Campanha campanha = id != null ? campanhaService.buscarPorId(id) : new Campanha();
        campanha.setNome(nome);
        campanha.setDescricao(descricao);
        campanha.setDataInicio(dataInicio);
        campanha.setDataFim(dataFim);
        campanha.setPublicoAlvo(publicoAlvo);
        campanhaService.cadastrarCampanha(campanha);
        redirectAttributes.addFlashAttribute("mensagem", "Campanha salva com sucesso!");
        return "redirect:/campanhas";
    }

    @PostMapping("/admin/campanhas/{id}/excluir")
    public String excluirCampanha(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            campanhaService.excluirCampanha(id);
            redirectAttributes.addFlashAttribute("mensagem", "Campanha excluida com sucesso!");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("erroValidacao", "Esta campanha possui vinculos e nao pode ser excluida.");
        }
        return "redirect:/campanhas";
    }

    @GetMapping("/usuarios")
    public String usuarios(Model model) {
        model.addAttribute("view", "usuarios");
        model.addAttribute("usuarios", usuarioService.listarUsuarios());
        return "index";
    }

    @PostMapping("/usuarios")
    public String cadastrarUsuario(@Valid @ModelAttribute UsuarioDTO dto,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("erroValidacao", result.getAllErrors().get(0).getDefaultMessage());
            return "redirect:/usuarios";
        }

        String email = dto.getEmail().trim().toLowerCase();
        if (usuarioService.emailJaCadastrado(email)) {
            redirectAttributes.addFlashAttribute("erroValidacao", "Ja existe um usuario cadastrado com esse e-mail.");
            return "redirect:/usuarios";
        }

        Usuario novo = new Usuario(dto.getNome(), dto.getIdade(), email, passwordEncoder.encode(dto.getSenha()), "USER");
        usuarioService.cadastrarUsuario(novo);
        redirectAttributes.addFlashAttribute("mensagem", "Usuario cadastrado com sucesso!");
        return "redirect:/usuarios";
    }

    private List<Vacina> buscarVacinas(List<Long> vacinaIds) {
        List<Vacina> vacinas = new ArrayList<>();
        if (vacinaIds == null) {
            return vacinas;
        }
        for (Long vacinaId : vacinaIds) {
            vacinas.add(vacinaService.buscarPorId(vacinaId));
        }
        return vacinas;
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication != null && authentication.getAuthorities().stream()
            .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
    }

    private void preencherCarteira(Model model, Authentication authentication) {
        Usuario usuario = usuarioService.buscarPorEmail(authentication.getName());
        List<RegistroVacinacao> registros = usuario.getListaRegistroVacinacao();
        List<Vacina> todasVacinas = vacinaService.listarVacinas();
        List<Vacina> pendentes = usuarioService.verificarPendencias(usuario.getId());
        List<Agendamento> agendamentos = agendamentoService.listarPorUsuario(usuario);

        Set<Long> vacinasAplicadasUnicas = new HashSet<>();
        for (RegistroVacinacao registro : registros) {
            if (registro.getVacina() != null && registro.getVacina().getId() != null) {
                vacinasAplicadasUnicas.add(registro.getVacina().getId());
            }
        }

        int totalVacinas = todasVacinas.size();
        int vacinasAplicadas = vacinasAplicadasUnicas.size();
        int coberturaVacinal = totalVacinas == 0 ? 0 : Math.round((vacinasAplicadas * 100f) / totalVacinas);
        int coberturaBucket = Math.min(100, Math.max(0, Math.round(coberturaVacinal / 10f) * 10));

        Optional<Agendamento> proximoAgendamento = agendamentos.stream()
            .filter(agendamento -> !"CANCELADO".equals(agendamento.getStatus()))
            .filter(agendamento -> !agendamento.getData().isBefore(LocalDate.now()))
            .min(Comparator.comparing(Agendamento::getData).thenComparing(Agendamento::getHorario));

        String proximaDoseValor = "Em dia";
        String proximaDoseDescricao = "Nenhuma dose pendente";
        if (proximoAgendamento.isPresent()) {
            Agendamento agendamento = proximoAgendamento.get();
            proximaDoseValor = agendamento.getData().toString();
            proximaDoseDescricao = agendamento.getVacina().getNome() + " as " + agendamento.getHorario();
        } else if (!pendentes.isEmpty()) {
            proximaDoseValor = "Pendente";
            proximaDoseDescricao = pendentes.get(0).getNome();
        }

        model.addAttribute("usuarioAtual", usuario);
        model.addAttribute("vacinas", todasVacinas);
        model.addAttribute("meusRegistros", registros);
        model.addAttribute("pendentes", pendentes);
        model.addAttribute("pendenciasCount", pendentes.size());
        model.addAttribute("meusAgendamentos", agendamentos);
        model.addAttribute("vacinasAplicadas", vacinasAplicadas);
        model.addAttribute("dosesRegistradas", registros.size());
        model.addAttribute("totalVacinas", totalVacinas);
        model.addAttribute("campanhasAtivas", campanhaService.listarCampanhas().size());
        model.addAttribute("coberturaVacinal", coberturaVacinal);
        model.addAttribute("coberturaClass", "progress-fill progress-fill--" + coberturaBucket);
        model.addAttribute("proximaDoseValor", proximaDoseValor);
        model.addAttribute("proximaDoseDescricao", proximaDoseDescricao);
    }
}
