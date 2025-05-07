package com.example.tarefa.service;

import com.example.tarefa.entity.Tarefa;
import com.example.tarefa.repository.TarefaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final TarefaRepository repo;
    private final EmailNotificationService notifier;

    @Transactional
    public Tarefa create(Tarefa tarefa) {
        Tarefa salva = tarefaRepository.save(tarefa);
        notifier.sendTaskNotification(salva, "CRIADA");
        return salva;
    }

    @Transactional(readOnly = true)
    public List<Tarefa> findAll() {
        List<Tarefa> tarefaList = new ArrayList<>();
        tarefaList = tarefaRepository.findAll();
        return tarefaList;
    }

    @Transactional(readOnly = true)
    public Tarefa findById(Long id) {
        return tarefaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada"));
    }

    @Transactional
    public Tarefa update(Long id, Tarefa tarefaAtualizada) {
        Tarefa tarefa = findById(id);
        tarefa.setDescricao(tarefaAtualizada.getDescricao());
        tarefa.setDataPrevista(tarefaAtualizada.getDataPrevista());
        tarefa.setDataEncerramento(tarefaAtualizada.getDataEncerramento());
        tarefa.setSituacao(tarefaAtualizada.getSituacao());
        Tarefa atualizada = tarefaRepository.save(tarefa);
        notifier.sendTaskNotification(atualizada, "ATUALIZADA");
        return atualizada;
    }

    @Transactional
    public void delete(Long id) {
        if (!tarefaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada");
        }
        tarefaRepository.deleteById(id);
    }

    public List<Tarefa> findByFiltro(LocalDate dataInicial,
                                     LocalDate dataFinal,
                                     String situacao) {

        LocalDateTime inicio = null;
        LocalDateTime fim    = null;

        if (dataInicial != null) {
            inicio = dataInicial.atStartOfDay();
        }
        if (dataFinal != null) {
            fim = dataFinal.atTime(LocalTime.MAX);
        }

        String sit = (situacao != null && !situacao.isBlank())
                ? situacao
                : null;

        return tarefaRepository.findByFiltro(inicio, fim, sit);
    }
}
