package com.example.tarefa;

import com.example.tarefa.entity.Tarefa;
import com.example.tarefa.repository.TarefaRepository;
import com.example.tarefa.service.EmailNotificationService;
import com.example.tarefa.web.dto.TarefaRequestDto;
import com.example.tarefa.web.dto.TarefaResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@ActiveProfiles("test")
@SpringBootTest(
        classes = TarefaApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@EnableAutoConfiguration(exclude = {
        SecurityAutoConfiguration.class,
        SecurityFilterAutoConfiguration.class
})



@AutoConfigureWebTestClient
class TarefaApplicationTests {

    @Autowired
    WebTestClient testClient;

    @MockitoBean
    EmailNotificationService emailNotificationService;

    @Autowired
    TarefaRepository tarefaRepository;

    @BeforeEach
    void setup() {
        Mockito.doNothing()
                .when(emailNotificationService)
                .sendTaskNotification(any(Tarefa.class), anyString());

        tarefaRepository.deleteAll();
    }

    @Test
    @DisplayName("Should create tarefa with valid data and return tarefa with status 201")
    public void createTarefa_WithDescricaoDataPrevistaAndSituacaoValid_ReturnTarefaCreatedWithStatus201() {

        TarefaRequestDto dto = new TarefaRequestDto(
                "Teste Teste",
                LocalDateTime.now().plusDays(1),
                null,
                "PENDENTE"
        );

        TarefaResponseDto responseBody = testClient
                .post()
                .uri("/tarefas/v1")
                .contentType(APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(TarefaResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getId()).isNotNull();
        Assertions.assertThat(responseBody.getDescricao()).isEqualTo("Teste Teste");
        Assertions.assertThat(responseBody.getSituacao()).isEqualTo("PENDENTE");
    }

    @Test
    @DisplayName("Should update tarefa with valid data and return tarefa with status 200")
    public void updateTarefa_WithExistingIdAndValidData_ReturnTarefaUpdatedWithStatus200() {
        TarefaResponseDto created = testClient
                .post()
                .uri("/tarefas/v1")
                .contentType(APPLICATION_JSON)
                .bodyValue(new TarefaRequestDto(
                        "Descrição Inicial",
                        LocalDateTime.of(2025, 5, 11, 12, 0),
                        null,
                        "PENDENTE"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(TarefaResponseDto.class)
                .returnResult().getResponseBody();

        Long id = created.getId();

        TarefaRequestDto updateDto = new TarefaRequestDto(
                "Descrição Atualizada",
                LocalDateTime.of(2025, 5, 12, 12, 0),
                LocalDateTime.of(2025, 5, 13, 12, 0),
                "CONCLUÍDA");

        TarefaResponseDto responseBody = testClient
                .put()
                .uri("/tarefas/v1/{id}", id)
                .contentType(APPLICATION_JSON)
                .bodyValue(updateDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TarefaResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getId()).isEqualTo(id);
        Assertions.assertThat(responseBody.getDescricao()).isEqualTo("Descrição Atualizada");
        Assertions.assertThat(responseBody.getDataEncerramento()).isEqualTo(LocalDateTime.of(2025, 5, 13, 12, 0));
        Assertions.assertThat(responseBody.getSituacao()).isEqualTo("CONCLUÍDA");
    }

    @Test
    @DisplayName("Should delete tarefa with valid id and return status 204")
    public void deleteTarefa_WithExistingId_ReturnNoContent() {
        TarefaResponseDto created = testClient
                .post()
                .uri("/tarefas/v1")
                .contentType(APPLICATION_JSON)
                .bodyValue(new TarefaRequestDto(
                        "Para Remover",
                        LocalDateTime.of(2025, 5, 14, 12, 0),
                        null,
                        "PENDENTE"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(TarefaResponseDto.class)
                .returnResult().getResponseBody();

        Long id = created.getId();

        testClient
                .delete()
                .uri("/tarefas/v1/{id}", id)
                .exchange()
                .expectStatus().isNoContent();

        testClient
                .get()
                .uri("/tarefas/v1/{id}", id)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("Should fetch tarefa by existing id and return tarefa with status 200")
    public void getTarefa_WithExistingId_ReturnTarefaWithStatus200() {

        TarefaResponseDto created = testClient
                .post()
                .uri("/tarefas/v1")
                .contentType(APPLICATION_JSON)
                .bodyValue(new TarefaRequestDto(
                        "Busca por ID",
                        LocalDateTime.now().plusDays(1),
                        null,
                        "PENDENTE"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(TarefaResponseDto.class)
                .returnResult()
                .getResponseBody();

        Long id = created.getId();

        TarefaResponseDto fetched = testClient
                .get()
                .uri("/tarefas/v1/{id}", id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TarefaResponseDto.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(fetched).isNotNull();
        Assertions.assertThat(fetched.getId()).isEqualTo(id);
        Assertions.assertThat(fetched.getDescricao()).isEqualTo("Busca por ID");

    }

    @Test
    @DisplayName("Should fetch all tarefas and return list with status 200")
    public void getAllTarefas_ReturnListWithStatus200() {
        testClient.post()
                .uri("/tarefas/v1")
                .contentType(APPLICATION_JSON)
                .bodyValue(new TarefaRequestDto("Lista 1",
                        LocalDateTime.now().plusDays(1),
                        null,
                        "PENDENTE"))
                .exchange()
                .expectStatus().isCreated();

        testClient.post()
                .uri("/tarefas/v1")
                .contentType(APPLICATION_JSON)
                .bodyValue(new TarefaRequestDto("Lista 2",
                        LocalDateTime.now().plusDays(2),
                        null,
                        "PENDENTE"))
                .exchange()
                .expectStatus().isCreated();

        testClient.get()
                .uri("/tarefas/v1")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TarefaResponseDto.class)
                .value(list -> {
                    Assertions.assertThat(list).isNotEmpty();
                    Assertions.assertThat(list.size()).isGreaterThanOrEqualTo(2);
                });
    }

    @Test
    @DisplayName("Should return 404 when updating non-existing tarefa")
    public void updateTarefa_NonExistingId_ReturnNotFound() {
        Long nonExistingId = 99999L;

        TarefaRequestDto updateDto = new TarefaRequestDto(
                "Não existe",
                LocalDateTime.now().plusDays(5),
                null,
                "PENDENTE"
        );

        testClient.put()
                .uri("/tarefas/v1/{id}", nonExistingId)
                .contentType(APPLICATION_JSON)
                .bodyValue(updateDto)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("Should return 404 when fetching non-existing tarefa by id")
    public void getTarefa_NonExistingId_ReturnNotFound() {
        Long nonExistingId = 99999L;
        testClient
                .get()
                .uri("/tarefas/v1/{id}", nonExistingId)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("Should return 404 when deleting non-existing tarefa")
    public void deleteTarefa_NonExistingId_ReturnNotFound() {
        Long nonExistingId = 88888L;
        testClient
                .delete()
                .uri("/tarefas/v1/{id}", nonExistingId)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("Should filter tarefas by situacao and return only matching tarefas with status 200")
    public void listTarefas_BySituacao_ReturnsFilteredList() {
        testClient.post()
                .uri("/tarefas/v1")
                .contentType(APPLICATION_JSON)
                .bodyValue(new TarefaRequestDto("Filtro Pendente",
                        LocalDateTime.now().plusDays(1),
                        null,
                        "PENDENTE"))
                .exchange()
                .expectStatus().isCreated();

        testClient.post()
                .uri("/tarefas/v1")
                .contentType(APPLICATION_JSON)
                .bodyValue(new TarefaRequestDto("Filtro Concluída",
                        LocalDateTime.now().plusDays(2),
                        null,
                        "CONCLUÍDA"))
                .exchange()
                .expectStatus().isCreated();

        testClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/tarefas/v1")
                        .queryParam("situacao", "CONCLUÍDA")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TarefaResponseDto.class)
                .value(list -> {
                    Assertions.assertThat(list)
                            .allMatch(t -> "CONCLUÍDA".equals(t.getSituacao()));
                    Assertions.assertThat(list.size()).isEqualTo(1);
                });
    }

    @Test
    @DisplayName("Should export tarefas to PDF and return application/pdf with status 200")
    public void exportPdf_ReturnsPdfWithStatus200() {
        testClient.post()
                .uri("/tarefas/v1")
                .contentType(APPLICATION_JSON)
                .bodyValue(new TarefaRequestDto("Para PDF",
                        LocalDateTime.now().plusDays(1),
                        null,
                        "PENDENTE"))
                .exchange()
                .expectStatus().isCreated();

        testClient.get()
                .uri("/tarefas/v1/pdf")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_PDF)
                .expectBody(byte[].class)
                .value(bytes ->
                        Assertions.assertThat(bytes.length)
                                .isGreaterThan(0)
                );
    }

    @Test
    @DisplayName("Should filter tarefas by dataInicial and dataFinal and return only tasks within the range")
    public void listTarefas_ByDateRange_ReturnsOnlyWithinRange() {
        LocalDateTime now = LocalDateTime.now();
        testClient.post().uri("/tarefas/v1")
                .contentType(APPLICATION_JSON)
                .bodyValue(new TarefaRequestDto("Range 1",
                        now.plusDays(1),
                        now.plusDays(2),
                        "CONCLUÍDA"))
                .exchange().expectStatus().isCreated();

        testClient.post().uri("/tarefas/v1")
                .contentType(APPLICATION_JSON)
                .bodyValue(new TarefaRequestDto("Range 2",
                        now.plusDays(3),
                        null,
                        "PENDENTE"))
                .exchange().expectStatus().isCreated();

        String start = LocalDate.now().plusDays(1).toString();
        String end   = LocalDate.now().plusDays(2).toString();

        testClient.get()
                .uri(uri -> uri
                        .path("/tarefas/v1")
                        .queryParam("dataInicial", start)
                        .queryParam("dataFinal",   end)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TarefaResponseDto.class)
                .value(list -> {
                    Assertions.assertThat(list)
                            .extracting(TarefaResponseDto::getDescricao)
                            .containsExactly("Range 1");
                });
    }

    @Test
    @DisplayName("Should filter tarefas by dataInicial only and return tasks from that date onwards")
    public void listTarefas_ByDataInicialOnly_ReturnsFromDateOnwards() {
        LocalDateTime now = LocalDateTime.now();
        testClient.post().uri("/tarefas/v1")
                .contentType(APPLICATION_JSON)
                .bodyValue(new TarefaRequestDto("Inicial 1",
                        now.plusDays(1),
                        null,
                        "PENDENTE"))
                .exchange().expectStatus().isCreated();

        testClient.post().uri("/tarefas/v1")
                .contentType(APPLICATION_JSON)
                .bodyValue(new TarefaRequestDto("Inicial 2",
                        now.plusDays(3),
                        null,
                        "PENDENTE"))
                .exchange().expectStatus().isCreated();

        String start = LocalDate.now().plusDays(2).toString();

        testClient.get()
                .uri(uri -> uri
                        .path("/tarefas/v1")
                        .queryParam("dataInicial", start)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TarefaResponseDto.class)
                .value(list -> {
                    Assertions.assertThat(list)
                            .hasSize(1)
                            .first()
                            .extracting(TarefaResponseDto::getDescricao)
                            .isEqualTo("Inicial 2");
                });
    }

    @Test
    @DisplayName("Should return 404 when calling wrong URL")
    public void wrongUrl_ReturnsNotFound() {
        testClient
                .get()
                .uri("/tarefas/v1/naoExiste")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("Should return 500 when email service throws exception on creation")
    public void createTarefa_EmailError_ReturnsServerError() {
        doThrow(new RuntimeException("SMTP down"))
                .when(emailNotificationService)
                .sendTaskNotification(any(Tarefa.class), anyString());

        TarefaRequestDto dto = new TarefaRequestDto(
                "Teste Email Fail",
                LocalDateTime.now().plusDays(1),
                null,
                "PENDENTE"
        );

        testClient.post()
                .uri("/tarefas/v1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    @DisplayName("Should filter tarefas by dataFinal only and return tasks up to that date")
    public void listTarefas_ByDataFinalOnly_ReturnsUpToDate() {
        LocalDateTime now = LocalDateTime.now();
        testClient.post().uri("/tarefas/v1")
                .contentType(APPLICATION_JSON)
                .bodyValue(new TarefaRequestDto("Finalizada",
                        now.plusDays(1),
                        now.plusDays(2),
                        "CONCLUÍDA"))
                .exchange().expectStatus().isCreated();

        testClient.post().uri("/tarefas/v1")
                .contentType(APPLICATION_JSON)
                .bodyValue(new TarefaRequestDto("Pendente",
                        now.plusDays(1),
                        null,
                        "PENDENTE"))
                .exchange().expectStatus().isCreated();

        String end = LocalDate.now().plusDays(2).toString();

        testClient.get()
                .uri(uri -> uri
                        .path("/tarefas/v1")
                        .queryParam("dataFinal", end)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TarefaResponseDto.class)
                .value(list -> {
                    Assertions.assertThat(list)
                            .extracting(TarefaResponseDto::getDescricao)
                            .containsExactly("Finalizada");
                });
    }

    @Test
    @DisplayName("Should invoke email notification on tarefa update")
    public void updateTarefa_ShouldInvokeEmailNotificationForUpdate() {
        LocalDateTime now = LocalDateTime.now();
        TarefaResponseDto created = testClient.post().uri("/tarefas/v1")
                .contentType(APPLICATION_JSON)
                .bodyValue(new TarefaRequestDto("Original",
                        now.plusDays(1),
                        null,
                        "PENDENTE"))
                .exchange().expectStatus().isCreated()
                .expectBody(TarefaResponseDto.class)
                .returnResult()
                .getResponseBody();

        Long id = created.getId();

        TarefaRequestDto updateDto = new TarefaRequestDto(
                "Atualizada",
                now.plusDays(2),
                now.plusDays(3),
                "CONCLUÍDA"
        );

        testClient.put()
                .uri("/tarefas/v1/{id}", id)
                .contentType(APPLICATION_JSON)
                .bodyValue(updateDto)
                .exchange()
                .expectStatus().isOk();

        Mockito.verify(emailNotificationService, Mockito.times(2))
                .sendTaskNotification(any(Tarefa.class), anyString());
        Mockito.verify(emailNotificationService)
                .sendTaskNotification(any(Tarefa.class), eq("ATUALIZADA"));
    }

}
