package com.example.tarefa.repository;

import com.example.tarefa.entity.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {


    @Query("""
    SELECT t FROM Tarefa t
     WHERE (:dataInicial IS NULL    OR t.dataPrevista   >= :dataInicial)
       AND (:dataFinal   IS NULL    OR t.dataEncerramento <= :dataFinal)
       AND (:situacao    IS NULL    OR t.situacao       = :situacao)
    """)
    List<Tarefa> findByFiltro(
            @Param("dataInicial") LocalDateTime dataInicial,
            @Param("dataFinal")   LocalDateTime dataFinal,
            @Param("situacao")    String situacao
    );
}
