package com.example.tarefa.web.dto.controller;

import com.example.tarefa.entity.Tarefa;
import com.example.tarefa.service.TarefaService;
import com.example.tarefa.web.dto.TarefaMapper;
import com.example.tarefa.web.dto.TarefaResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tarefas/v1")
public class TarefaController {

    private final TarefaService tarefaService;

    @GetMapping
    public ResponseEntity<List<TarefaResponseDto>> listarTodos() {
        List<Tarefa> tarefas = tarefaService.findAll();
        return ResponseEntity.ok().body(TarefaMapper.toListDto(tarefas));
    }

}
