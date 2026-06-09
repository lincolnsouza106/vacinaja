package br.com.vacinaja.config;

import br.com.vacinaja.model.Campanha;
import br.com.vacinaja.model.PostoSaude;
import br.com.vacinaja.model.Usuario;
import br.com.vacinaja.model.Vacina;
import br.com.vacinaja.repository.CampanhaRepository;
import br.com.vacinaja.repository.PostoSaudeRepository;
import br.com.vacinaja.repository.UsuarioRepository;
import br.com.vacinaja.repository.VacinaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Calendar;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UsuarioRepository usuarioRepository,
                                   VacinaRepository vacinaRepository,
                                   PostoSaudeRepository postoRepository,
                                   CampanhaRepository campanhaRepository,
                                   PasswordEncoder passwordEncoder) {
        return args -> {
            if (usuarioRepository.count() == 0) {
                usuarioRepository.save(new Usuario("Administrador", 35, "admin@vacinaja.com", passwordEncoder.encode("admin123"), "ADMIN"));
                usuarioRepository.save(new Usuario("Joao Silva", 25, "joao@vacinaja.com", passwordEncoder.encode("usuario123"), "USER"));
                usuarioRepository.save(new Usuario("Maria Santos", 68, "maria@vacinaja.com", passwordEncoder.encode("usuario123"), "USER"));

                Vacina febreAmarela = new Vacina("Febre Amarela", "Febre Amarela", 0, 100, 1, 3650);
                Vacina gripe = new Vacina("Gripe", "Influenza", 0, 100, 1, 365);
                vacinaRepository.save(febreAmarela);
                vacinaRepository.save(gripe);

                PostoSaude ubs1 = new PostoSaude("UBS Jardim America", "Av. das Nacoes, 789", "Zona Sul", "Seg-Sex 7h-19h", "(11) 5555-1234");
                ubs1.adicionarVacinaDisponivel(febreAmarela);
                ubs1.adicionarVacinaDisponivel(gripe);
                postoRepository.save(ubs1);

                Calendar calInicio = Calendar.getInstance();
                calInicio.set(2026, Calendar.FEBRUARY, 1);
                Calendar calFim = Calendar.getInstance();
                calFim.set(2026, Calendar.MARCH, 28);
                campanhaRepository.save(new Campanha("Carnaval Seguro", "Reforco contra Febre Amarela", calInicio.getTime(), calFim.getTime(), "Viajantes"));
            }

            if (!usuarioRepository.existsByEmail("admin@vacinaja.com")) {
                usuarioRepository.save(new Usuario("Administrador", 35, "admin@vacinaja.com", passwordEncoder.encode("admin123"), "ADMIN"));
            }
        };
    }
}
