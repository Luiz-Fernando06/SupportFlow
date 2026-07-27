package com.supportflow.backend.service;

import com.supportflow.backend.dto.response.HistoricoChamadoResponse;
import com.supportflow.backend.dto.response.UsuarioResponse;
import com.supportflow.backend.enums.Role;
import com.supportflow.backend.enums.StatusChamado;
import com.supportflow.backend.exception.AcessoNegadoException;
import com.supportflow.backend.exception.ChamadoNaoEncontradoException;
import com.supportflow.backend.exception.UsuarioNaoEncontradoException;
import com.supportflow.backend.model.Chamado;
import com.supportflow.backend.model.HistoricoDeChamado;
import com.supportflow.backend.model.Usuario;
import com.supportflow.backend.repository.ChamadoRepository;
import com.supportflow.backend.repository.HistoricoDeChamadoRepository;
import com.supportflow.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoricoDeChamadoService {

    private final HistoricoDeChamadoRepository historicoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ChamadoRepository chamadoRepository;

    public HistoricoChamadoResponse registrar(
            Chamado chamado,
            StatusChamado statusAnterior,
            StatusChamado statusNovo
    ) {

        Usuario usuario = usuarioLogado();

        HistoricoDeChamado historico = new HistoricoDeChamado(
                chamado,
                statusAnterior,
                statusNovo,
                usuario
        );

        historicoRepository.save(historico);

        return respostaDe(historico);
    }

    public List<HistoricoChamadoResponse> listarPorChamado(Long chamadoId) {

        Chamado chamado = chamadoRepository.findById(chamadoId)
                .orElseThrow(() ->
                        new ChamadoNaoEncontradoException("Chamado não encontrado."));

        Usuario usuario = usuarioLogado();

        if (usuario.getRole() != Role.ADMIN &&
                !chamado.getUsuario().getId().equals(usuario.getId())) {

            throw new AcessoNegadoException(
                    "Você não possui acesso ao histórico deste chamado.");
        }

        List<HistoricoDeChamado> historicos =
                historicoRepository.findByChamado(chamado);

        List<HistoricoChamadoResponse> respostas =
                new ArrayList<>();

        for (HistoricoDeChamado historico : historicos) {
            respostas.add(respostaDe(historico));
        }

        return respostas;
    }

    private Usuario usuarioLogado() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return usuarioRepository.findUsuarioByEmail(email)
                .orElseThrow(() ->
                        new UsuarioNaoEncontradoException("Usuário não encontrado."));
    }

    private HistoricoChamadoResponse respostaDe(HistoricoDeChamado historico) {

        return new HistoricoChamadoResponse(
                historico.getId(),
                historico.getStatusAnterior(),
                historico.getStatusNovo(),
                respostaDe(historico.getAlteradoPor()),
                historico.getCriadoEm()
        );
    }

    private UsuarioResponse respostaDe(Usuario usuario) {

        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole(),
                usuario.getCriadoEm()
        );
    }
}