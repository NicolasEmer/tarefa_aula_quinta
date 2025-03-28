package com.example.tarefa.service;

import com.example.tarefa.entity.Tarefa;
import com.example.tarefa.repository.TarefaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TarefaService {

    private final TarefaRepository tarefaRepository;

    @Transactional(readOnly = true)
    public List<Tarefa> findAll() {
        List<Tarefa> tarefaList = new ArrayList<>();
        tarefaList = tarefaRepository.findAll();
        return tarefaList;
    }

}
