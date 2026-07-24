package com.supportflow.backend.repository;

import com.supportflow.backend.enums.Prioridade;
import com.supportflow.backend.enums.StatusChamado;
import com.supportflow.backend.model.Categoria;
import com.supportflow.backend.model.Chamado;
import com.supportflow.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChamadoRepository extends JpaRepository<Chamado, Long> {

    List<Chamado> findByUsuario(Usuario usuario);

    List<Chamado> findByStatus(StatusChamado status);

    List<Chamado> findByCategoria(Categoria categoria);

    List<Chamado> findByPrioridade(Prioridade prioridade);

}
