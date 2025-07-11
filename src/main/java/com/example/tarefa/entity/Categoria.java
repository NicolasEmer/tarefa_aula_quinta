package com.example.tarefa.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categoria")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String descricao;
}
