package com.supportflow.backend.repository;

import com.supportflow.backend.model.Chamado;
import com.supportflow.backend.model.SalaDeAtendimento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SalaDeAtendimentoRepository extends JpaRepository<SalaDeAtendimento, Long> {

    Optional<SalaDeAtendimento> findByChamado(Chamado chamado);

}
