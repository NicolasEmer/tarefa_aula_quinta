package com.example.tarefa.web.dto;

import com.example.tarefa.entity.Tarefa;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

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

}
