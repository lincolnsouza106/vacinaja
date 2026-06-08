package br.com.vacinaja.controller;

import br.com.vacinaja.model.PostoSaude;
import br.com.vacinaja.service.PostoService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/postos")
@CrossOrigin(origins = "*")
public class PostoSaudeController {
    private final PostoService postoService;

    public PostoSaudeController(PostoService postoService) {
        this.postoService = postoService;
    }

    @GetMapping
    public List<PostoSaude> listarPostos() {
        return postoService.listarPostos();
    }

    @GetMapping("/buscar")
    public List<PostoSaude> buscarPorRegiaoEVacina(
            @RequestParam(required = false) String regiao,
            @RequestParam(required = false) String vacina) {
        return postoService.buscarPorRegiaoEVacina(regiao, vacina);
    }

    @PostMapping
    public PostoSaude cadastrarPosto(@RequestBody PostoSaude posto) {
        return postoService.cadastrarPosto(posto);
    }
}
