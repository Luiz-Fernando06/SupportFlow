package com.supportflow.backend.service;

import com.supportflow.backend.exception.RegraDeNegocioException;
import com.supportflow.backend.model.Chamado;
import com.supportflow.backend.model.SalaDeAtendimento;
import com.supportflow.backend.repository.SalaDeAtendimentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SalaDeAtendimentoService {

    private final SalaDeAtendimentoRepository salaDeAtendimentoRepository;

    public SalaDeAtendimento criar(Chamado chamado) {

        SalaDeAtendimento sala = new SalaDeAtendimento(chamado);

        return salaDeAtendimentoRepository.save(sala);
    }

    public SalaDeAtendimento buscarPorChamado(Chamado chamado) {

        return salaDeAtendimentoRepository.findByChamado(chamado)
                .orElseThrow(() ->
                        new RegraDeNegocioException("Sala de atendimento não encontrada."));
    }

    public SalaDeAtendimento salvar(SalaDeAtendimento sala) {
        return salaDeAtendimentoRepository.save(sala);
    }
}