package com.supportflow.backend.service;

import com.supportflow.backend.dto.response.*;
import com.supportflow.backend.exception.ChamadoNaoEncontradoException;
import com.supportflow.backend.exception.RegraDeNegocioException;
import com.supportflow.backend.model.Categoria;
import com.supportflow.backend.model.Chamado;
import com.supportflow.backend.model.SalaDeAtendimento;
import com.supportflow.backend.model.Usuario;
import com.supportflow.backend.repository.ChamadoRepository;
import com.supportflow.backend.repository.SalaDeAtendimentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SalaDeAtendimentoService {

    private final SalaDeAtendimentoRepository salaDeAtendimentoRepository;
    private final ChamadoRepository chamadoRepository;

    public SalaDeAtendimento criar(Chamado chamado) {

        SalaDeAtendimento sala = new SalaDeAtendimento(chamado);

        return salaDeAtendimentoRepository.save(sala);
    }

    public SalaDeAtendimento buscarPorChamado(Chamado chamado) {

        return salaDeAtendimentoRepository.findByChamado(chamado)
                .orElseThrow(() ->
                        new RegraDeNegocioException("Sala de atendimento não encontrada."));
    }

    public SalaDeAtendimentoResponse buscarPorChamado(Long chamadoId) {

        Chamado chamado = chamadoRepository.findById(chamadoId)
                .orElseThrow(() ->
                        new ChamadoNaoEncontradoException("Chamado não encontrado."));

        SalaDeAtendimento sala = salaDeAtendimentoRepository
                .findByChamado(chamado)
                .orElseThrow(() ->
                        new RegraDeNegocioException("Sala não encontrada."));

        return respostaDe(sala);
    }

    public SalaDeAtendimento buscarPorId(Long id) {

        return salaDeAtendimentoRepository.findById(id)
                .orElseThrow(() ->
                        new RegraDeNegocioException("Sala não encontrada."));
    }

    public SalaDeAtendimento salvar(SalaDeAtendimento sala) {
        return salaDeAtendimentoRepository.save(sala);
    }

    private SalaDeAtendimentoResponse respostaDe(
            SalaDeAtendimento sala
    ) {

        if (sala == null) {
            return null;
        }

        return new SalaDeAtendimentoResponse(

                sala.getId(),

                new ChamadoResumoResponse(

                        sala.getChamado().getId(),

                        sala.getChamado().getTitulo(),

                        sala.getChamado().getStatus(),

                        sala.getChamado().getPrioridade(),

                        respostaDe(sala.getChamado().getCategoria()),

                        respostaDe(sala.getChamado().getUsuario()),

                        respostaDe(sala.getChamado().getAdminResponsavel()),

                        sala.getChamado().getCriadoEm()
                ),

                sala.isAtiva(),

                sala.getCriadoEm()
        );
    }

    private CategoriaResponse respostaDe(Categoria categoria) {

        return new CategoriaResponse(

                categoria.getId(),

                categoria.getNome(),

                categoria.getDescricao()
        );
    }

    private UsuarioResponse respostaDe(Usuario usuario) {

        if (usuario == null) {
            return null;
        }

        return new UsuarioResponse(

                usuario.getId(),

                usuario.getNome(),

                usuario.getEmail(),

                usuario.getRole(),

                usuario.getCriadoEm()
        );
    }
}