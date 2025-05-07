package com.example.tarefa.web.dto;

import com.example.tarefa.entity.Tarefa;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TarefaMapper {

    public static TarefaResponseDto toDto(Tarefa post) {
        return new ModelMapper().map(post, TarefaResponseDto.class);
    }

    public static List<TarefaResponseDto> toListDto(List<Tarefa> tarefas) {
        return tarefas.stream().map(tarefa->toDto(tarefa)).collect(Collectors.toList());
    }

    public static Tarefa toEntity(TarefaRequestDto dto) {
        Tarefa tarefa = new Tarefa();
        tarefa.setDescricao(dto.getDescricao());
        tarefa.setDataCriacao(LocalDateTime.now());
        tarefa.setDataPrevista(dto.getDataPrevista());
        tarefa.setDataEncerramento(dto.getDataEncerramento());
        tarefa.setSituacao(dto.getSituacao());
        return tarefa;
    }

    public static void updateEntity(Tarefa tarefa, TarefaRequestDto dto) {
        tarefa.setDescricao(dto.getDescricao());
        tarefa.setDataPrevista(dto.getDataPrevista());
        tarefa.setDataEncerramento(dto.getDataEncerramento());
        tarefa.setSituacao(dto.getSituacao());
    }

}
