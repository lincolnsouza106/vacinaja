package br.com.vacinaja.controller;

import br.com.vacinaja.model.Usuario;
import br.com.vacinaja.model.Vacina;
import br.com.vacinaja.model.RegistroVacinacao;
import br.com.vacinaja.service.UsuarioService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    @PostMapping
    public Usuario cadastrarUsuario(@RequestBody Usuario usuario) {
        return usuarioService.cadastrarUsuario(usuario);
    }

    @GetMapping("/{id}/pendencias")
    public List<Vacina> verificarPendencias(@PathVariable Long id) {
        return usuarioService.verificarPendencias(id);
    }

    @PostMapping("/registro")
    public RegistroVacinacao registrarVacinacao(@RequestBody RegistroVacinacao registro) {
        return usuarioService.registrarVacinacao(registro);
    }
}
