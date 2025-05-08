package com.example.tarefa;

import com.example.tarefa.entity.Usuario;
import com.example.tarefa.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test2")
class LoginMvcTests {

    @Autowired MockMvc mockMvc;
    @Autowired UsuarioRepository usuarioRepo;
    @Autowired PasswordEncoder passwordEncoder;

    @TestConfiguration
    static class PasswordEncoderConfig {
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }


    @BeforeEach
    void setup() {
        usuarioRepo.deleteAll();
        usuarioRepo.save(new Usuario(
                null,
                "john",
                "john@example.com",
                passwordEncoder.encode("secret")
        ));
    }

    @Test
    @DisplayName("Login with valid credentials should redirect to '/'")
    void loginComSucesso_DeveRedirecionarParaHome() throws Exception {
        mockMvc.perform(
                        SecurityMockMvcRequestBuilders.formLogin("/login")
                                .user("username", "john")
                                .password("password", "secret")
                )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    @DisplayName("Login with invalid credentials should redirect to '/login?error'")
    void loginIncorreto_DeveRedirecionarParaError() throws Exception {
        mockMvc.perform(
                        SecurityMockMvcRequestBuilders.formLogin("/login")
                                .user("username", "john")
                                .password("password", "wrongpass")
                )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    @DisplayName("GET /login should return login page with status 200")
    void getLoginPage_ReturnsOk() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /logout should redirect to '/login?logout'")
    void logout_ShouldRedirectToLoginWithLogoutParam() throws Exception {
        mockMvc.perform(logout())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login?logout"));
    }

}
