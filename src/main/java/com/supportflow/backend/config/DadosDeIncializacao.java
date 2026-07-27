package com.supportflow.backend.config;

import com.supportflow.backend.enums.Role;
import com.supportflow.backend.model.Categoria;
import com.supportflow.backend.model.Usuario;
import com.supportflow.backend.repository.CategoriaRepository;
import com.supportflow.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DadosDeIncializacao {

    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${ADMIN_NAME}")
    String adminNome;

    @Value("${ADMIN_EMAIL}")
    String adminEmail;

    @Value("${ADMIN_PASSWORD}")
    String adminSenha;

    @Bean
    public CommandLineRunner inicialize() {
        return args -> {
            criarCategorias();
            criarAdmin();
        };
    }

    private void criarCategorias() {

        salvar("Hadware", "Problemas físicos.");
        salvar("Software", "Problemas relacionados a programa e sistemas.");
        salvar("Rede", "Problemas de conexão.");
        salvar("Acesso", "Problemas de acesso em um sistema.");
        salvar("Outros", "Problemas físicos.");

    }

    private void salvar(String nome, String descricao) {
        if (!categoriaRepository.existsByNome(nome)) {
            categoriaRepository.save(new Categoria(nome, descricao));
        }
    }

    private void criarAdmin() {
        if (usuarioRepository.existsByRole(Role.ADMIN)) {
            return;
        }

        String adminSenhaHash = passwordEncoder.encode(adminSenha);

        Usuario admin = new Usuario(adminNome, adminEmail, adminSenhaHash, Role.ADMIN);

        usuarioRepository.save(admin);

    }
}
