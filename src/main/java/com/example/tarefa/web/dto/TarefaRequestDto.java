package com.example.tarefa.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TarefaRequestDto {

    private String descricao;
    private LocalDateTime dataPrevista;
    private LocalDateTime dataEncerramento;
    private String situacao;

}
