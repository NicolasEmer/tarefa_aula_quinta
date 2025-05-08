package com.example.tarefa.entity;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "descricao", length = 100, nullable = false)
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
