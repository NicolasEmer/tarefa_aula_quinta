package com.example.tarefa.web.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TarefaResponseDto {

    private Long id;
    private String descricao;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataPrevista;
    private LocalDateTime dataEncerramento;
    private String situacao;

}
