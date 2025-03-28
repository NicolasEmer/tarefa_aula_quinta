package com.example.tarefa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tarefa")
public class Tarefa {

    @Id
    private Long id;
    @Column(name = "descricao", length = 100)
    private String descricao;
    @Column(name="data_criacao")
    private LocalDateTime dataCriacao;
    @Column(name="data_prevista")
    private LocalDateTime dataPrevista;
    @Column(name="data_encerramento")
    private LocalDateTime dataEncerramento;
    @Column(name = "situacao", length = 20)
    private String situacao;


}
