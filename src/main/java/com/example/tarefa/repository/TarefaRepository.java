package com.example.tarefa.repository;

import com.example.tarefa.entity.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {



}
