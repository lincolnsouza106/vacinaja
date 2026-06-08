package br.com.vacinaja.controller;

import br.com.vacinaja.model.Vacina;
import br.com.vacinaja.service.VacinaService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/vacinas")
@CrossOrigin(origins = "*")
public class VacinaController {
    private final VacinaService vacinaService;

    public VacinaController(VacinaService vacinaService) {
        this.vacinaService = vacinaService;
    }

    @GetMapping
    public List<Vacina> listarVacinas() {
        return vacinaService.listarVacinas();
    }

    @GetMapping("/idade/{idade}")
    public List<Vacina> consultarPorIdade(@PathVariable int idade) {
        return vacinaService.consultarPorIdade(idade);
    }

    @PostMapping
    public Vacina cadastrarVacina(@RequestBody Vacina vacina) {
        return vacinaService.cadastrarVacina(vacina);
    }
}
