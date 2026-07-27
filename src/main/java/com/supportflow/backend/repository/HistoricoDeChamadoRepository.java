package com.supportflow.backend.repository;

import com.supportflow.backend.dto.response.HistoricoChamadoResponse;
import com.supportflow.backend.model.Chamado;
import com.supportflow.backend.model.HistoricoDeChamado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoricoDeChamadoRepository extends JpaRepository <HistoricoDeChamado, Long> {

    List<HistoricoDeChamado> findByChamado(Chamado chamado);

}
