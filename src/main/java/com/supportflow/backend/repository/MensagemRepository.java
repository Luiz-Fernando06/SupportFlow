package com.supportflow.backend.repository;

import com.supportflow.backend.model.Mensagem;
import com.supportflow.backend.model.SalaDeAtendimento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MensagemRepository extends JpaRepository<Mensagem, Long> {

    List<Mensagem> findBySala(SalaDeAtendimento sala);

}
