package com.example.tarefa.web.dto.controller;

import com.example.tarefa.entity.Tarefa;
import com.example.tarefa.service.TarefaService;
import com.example.tarefa.web.dto.TarefaMapper;
import com.example.tarefa.web.dto.TarefaRequestDto;
import com.example.tarefa.web.dto.TarefaResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tarefas/v1")
public class TarefaController {

    private final TarefaService tarefaService;

    @GetMapping
    public ResponseEntity<List<TarefaResponseDto>> listarTodos(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dataInicial,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dataFinal,

            @RequestParam(required = false)
            String situacao
    ) {
        List<Tarefa> tarefas = tarefaService.findByFiltro(dataInicial, dataFinal, situacao);
        return ResponseEntity.ok(TarefaMapper.toListDto(tarefas));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TarefaResponseDto> buscarPorId(@PathVariable Long id) {
        Tarefa tarefa = tarefaService.findById(id);
        return ResponseEntity.ok(TarefaMapper.toDto(tarefa));
    }

    @PostMapping
    public ResponseEntity<TarefaResponseDto> criar(@RequestBody TarefaRequestDto dto) {
        Tarefa tarefa = TarefaMapper.toEntity(dto);
        Tarefa criada = tarefaService.create(tarefa);
        return ResponseEntity.status(HttpStatus.CREATED).body(TarefaMapper.toDto(criada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarefaResponseDto> atualizar(
            @PathVariable Long id,
            @RequestBody TarefaRequestDto dto
    ) {
        Tarefa tarefaAtualizada = TarefaMapper.toEntity(dto);
        Tarefa atual = tarefaService.update(id, tarefaAtualizada);
        return ResponseEntity.ok(TarefaMapper.toDto(atual));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        tarefaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
