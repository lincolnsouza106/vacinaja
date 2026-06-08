package br.com.vacinaja.controller;

import br.com.vacinaja.model.Campanha;
import br.com.vacinaja.service.CampanhaService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/campanhas")
@CrossOrigin(origins = "*")
public class CampanhaController {
    private final CampanhaService campanhaService;

    public CampanhaController(CampanhaService campanhaService) {
        this.campanhaService = campanhaService;
    }

    @GetMapping
    public List<Campanha> listarCampanhas() {
        return campanhaService.listarCampanhas();
    }

    @PostMapping
    public Campanha cadastrarCampanha(@RequestBody Campanha campanha) {
        return campanhaService.cadastrarCampanha(campanha);
    }
}
