package com.supportflow.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="mensagens")
public class Mensagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="sala_id", nullable = false)
    private SalaDeAtendimento sala;

    @ManyToOne
    @JoinColumn(name = "remetente_id", nullable = false)
    private Usuario remetente;

    @Column(length = 2000)
    private String conteudo;

    @CreationTimestamp
    @Column(nullable = false, name="enviada_em")
    private LocalDateTime enviadaEm;

    public Mensagem(SalaDeAtendimento sala, Usuario remetente, String conteudo) {
        this.sala = sala;
        this.remetente = remetente;
        this.conteudo = conteudo;
    }
}
