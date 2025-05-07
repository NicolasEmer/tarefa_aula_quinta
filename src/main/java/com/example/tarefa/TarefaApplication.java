package com.example.tarefa;

import com.example.tarefa.entity.Usuario;
import com.example.tarefa.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.TimeZone;

@SpringBootApplication
public class TarefaApplication {

    public static void main(String[] args) {

        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));

        SpringApplication.run(TarefaApplication.class, args);
    }

    @Bean
    public CommandLineRunner initAdmin(UsuarioRepository userRepo,
                                       PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepo.findByUsername("admin").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setUsername("admin");
                admin.setEmail("nicolasemer2211@gmail.com");
                admin.setPassword(passwordEncoder.encode("admin"));
                userRepo.save(admin);
                System.out.println("Usuário padrão 'admin' criado com sucesso.");
            }
        };
    }

}
