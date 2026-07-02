package com.supportflow.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="salas_atendimento")
public class SalaDeAtendimento {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="chamado_id", nullable = false, unique = true)
    private Chamado chamado;

    @Column(nullable = false)
    private boolean ativa;

    @CreationTimestamp
    @Column(nullable = false, name="criado_em")
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    @Column(nullable = false, name="atualizado_em")
    private LocalDateTime atualizadoEm;

    public SalaDeAtendimento(Chamado chamado) {
        this.chamado = chamado;
        this.ativa = true;
    }
}
