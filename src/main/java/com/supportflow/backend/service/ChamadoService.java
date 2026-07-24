package com.supportflow.backend.service;

import com.supportflow.backend.dto.request.AlterarStatusRequest;
import com.supportflow.backend.dto.request.CriarChamadoRequest;
import com.supportflow.backend.dto.response.*;
import com.supportflow.backend.enums.Prioridade;
import com.supportflow.backend.enums.Role;
import com.supportflow.backend.enums.StatusChamado;
import com.supportflow.backend.exception.AcessoNegadoException;
import com.supportflow.backend.exception.RegraDeNegocioException;
import com.supportflow.backend.exception.UsuarioNaoEncontradoException;
import com.supportflow.backend.model.Categoria;
import com.supportflow.backend.model.Chamado;
import com.supportflow.backend.model.SalaDeAtendimento;
import com.supportflow.backend.model.Usuario;
import com.supportflow.backend.repository.ChamadoRepository;
import com.supportflow.backend.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChamadoService {

    private final ChamadoRepository chamadoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaService categoriaService;
    private final SalaDeAtendimentoService salaDeAtendimentoService;
    private final HistoricoDeChamadoService historicoDeChamadoService;

    @Transactional
    public ChamadoResponse criar(CriarChamadoRequest request) {

        Usuario usuario = usuarioLogado();

        Categoria categoria =
                categoriaService.buscarPorId(request.categoriaId());

        Chamado chamado = new Chamado(
                request.titulo().trim(),
                request.descricao().trim(),
                request.prioridade(),
                usuario,
                categoria
        );

        chamadoRepository.save(chamado);

        SalaDeAtendimento sala =
                salaDeAtendimentoService.criar(chamado);

        return respostaDe(chamado, sala);
    }

    public ChamadoResponse buscarPorId(Long id) {

        Usuario usuario = usuarioLogado();

        Chamado chamado = buscarChamado(id);

        if (usuario.getRole().name().equals("USER")
                && !chamado.getUsuario().getId().equals(usuario.getId())) {

            throw new AcessoNegadoException(
                    "Você não possui acesso a este chamado."
            );
        }

        SalaDeAtendimento sala =
                salaDeAtendimentoService.buscarPorChamado(chamado);

        return respostaDe(chamado, sala);
    }

    public List<ChamadoResponse> listarMeusChamados() {

        Usuario usuario = usuarioLogado();

        List<Chamado> chamados =
                chamadoRepository.findByUsuario(usuario);

        List<ChamadoResponse> respostas =
                new ArrayList<>();

        for (Chamado chamado : chamados) {

            SalaDeAtendimento sala =
                    salaDeAtendimentoService.buscarPorChamado(chamado);

            respostas.add(respostaDe(chamado, sala));
        }

        return respostas;
    }

    public List<ChamadoResponse> listarTodos() {

        List<Chamado> chamados =
                chamadoRepository.findAll();

        List<ChamadoResponse> respostas =
                new ArrayList<>();

        for (Chamado chamado : chamados) {

            SalaDeAtendimento sala =
                    salaDeAtendimentoService.buscarPorChamado(chamado);

            respostas.add(respostaDe(chamado, sala));
        }

        return respostas;
    }

    public List<ChamadoResponse> listarPorStatus(StatusChamado status) {

        List<Chamado> chamados =
                chamadoRepository.findByStatus(status);

        List<ChamadoResponse> respostas =
                new ArrayList<>();

        for (Chamado chamado : chamados) {

            SalaDeAtendimento sala =
                    salaDeAtendimentoService.buscarPorChamado(chamado);

            respostas.add(respostaDe(chamado, sala));
        }

        return respostas;
    }

    public List<ChamadoResponse> listarPorPrioridade(Prioridade prioridade) {

        List<Chamado> chamados =
                chamadoRepository.findByPrioridade(prioridade);

        List<ChamadoResponse> respostas =
                new ArrayList<>();

        for (Chamado chamado : chamados) {

            SalaDeAtendimento sala =
                    salaDeAtendimentoService.buscarPorChamado(chamado);

            respostas.add(respostaDe(chamado, sala));
        }

        return respostas;
    }

    public List<ChamadoResponse> listarPorCategoria(Long categoriaId) {

        Categoria categoria =
                categoriaService.buscarPorId(categoriaId);

        List<Chamado> chamados =
                chamadoRepository.findByCategoria(categoria);

        List<ChamadoResponse> respostas =
                new ArrayList<>();

        for (Chamado chamado : chamados) {

            SalaDeAtendimento sala =
                    salaDeAtendimentoService.buscarPorChamado(chamado);

            respostas.add(respostaDe(chamado, sala));
        }

        return respostas;
    }

    @Transactional
    public ChamadoResponse cancelarChamado(Long chamadoId) {

        Usuario usuario = usuarioLogado();

        Chamado chamado = buscarChamado(chamadoId);

        if (!chamado.getUsuario().getId().equals(usuario.getId())
                && usuario.getRole() != Role.ADMIN) {

            throw new RegraDeNegocioException(
                    "Você não possui permissão para cancelar este chamado."
            );
        }

        if (chamado.getStatus() == StatusChamado.RESOLVIDO) {
            throw new RegraDeNegocioException(
                    "Chamados resolvidos não podem ser cancelados."
            );
        }

        StatusChamado statusAnterior = chamado.getStatus();

        chamado.setStatus(StatusChamado.CANCELADO);

        chamadoRepository.save(chamado);

        historicoDeChamadoService.registrar(
                chamado,
                statusAnterior,
                StatusChamado.CANCELADO
        );

        SalaDeAtendimento sala =
                salaDeAtendimentoService.buscarPorChamado(chamado);

        sala.setAtiva(false);

        salaDeAtendimentoService.salvar(sala);

        return respostaDe(chamado, sala);
    }

    @Transactional
    public ChamadoResponse assumirChamado(Long chamadoId) {

        Usuario admin = usuarioLogado();

        if (admin.getRole() != Role.ADMIN) {
            throw new RegraDeNegocioException(
                    "Apenas administradores podem assumir chamados."
            );
        }

        Chamado chamado = buscarChamado(chamadoId);

        if (chamado.getStatus() != StatusChamado.ABERTO) {
            throw new RegraDeNegocioException(
                    "Somente chamados ABERTOS podem ser assumidos."
            );
        }

        chamado.setAdminResponsavel(admin);

        chamadoRepository.save(chamado);

        SalaDeAtendimento sala =
                salaDeAtendimentoService.buscarPorChamado(chamado);

        return respostaDe(chamado, sala);
    }

    @Transactional
    public ChamadoResponse alterarStatus(
            Long chamadoId,
            AlterarStatusRequest request) {

        Usuario admin = usuarioLogado();

        if (admin.getRole() != Role.ADMIN) {
            throw new RegraDeNegocioException(
                    "Apenas administradores podem alterar o status."
            );
        }

        Chamado chamado = buscarChamado(chamadoId);

        StatusChamado anterior = chamado.getStatus();
        StatusChamado novo = request.status();

        validarMudancaStatus(anterior, novo);

        chamado.setStatus(novo);

        chamadoRepository.save(chamado);

        historicoDeChamadoService.registrar(
                chamado,
                anterior,
                novo
        );

        SalaDeAtendimento sala =
                salaDeAtendimentoService.buscarPorChamado(chamado);

        if (novo == StatusChamado.RESOLVIDO
                || novo == StatusChamado.CANCELADO) {

            sala.setAtiva(false);
            salaDeAtendimentoService.salvar(sala);

        } else if (anterior == StatusChamado.RESOLVIDO
                && novo == StatusChamado.ABERTO) {

            sala.setAtiva(true);
            salaDeAtendimentoService.salvar(sala);
        }

        return respostaDe(chamado, sala);
    }

    private Chamado buscarChamado(Long id) {

        return chamadoRepository.findById(id)
                .orElseThrow(() ->
                        new RegraDeNegocioException(
                                "Chamado não encontrado."
                        ));
    }

    private Usuario usuarioLogado() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return usuarioRepository.findUsuarioByEmail(email)
                .orElseThrow(() ->
                        new UsuarioNaoEncontradoException(
                                "Usuário não encontrado."
                        ));
    }

    private void validarMudancaStatus(
            StatusChamado anterior,
            StatusChamado novo) {

        if (anterior == StatusChamado.ABERTO
                && novo == StatusChamado.EM_ANDAMENTO) {
            return;
        }

        if (anterior == StatusChamado.EM_ANDAMENTO
                && novo == StatusChamado.RESOLVIDO) {
            return;
        }

        if (anterior == StatusChamado.ABERTO
                && novo == StatusChamado.CANCELADO) {
            return;
        }

        if (anterior == StatusChamado.EM_ANDAMENTO
                && novo == StatusChamado.CANCELADO) {
            return;
        }

        if (anterior == StatusChamado.RESOLVIDO
                && novo == StatusChamado.ABERTO) {
            return;
        }

        throw new RegraDeNegocioException(
                "Mudança de status não permitida."
        );
    }

    private ChamadoResponse respostaDe(
            Chamado chamado,
            SalaDeAtendimento sala
    ) {

        return new ChamadoResponse(

                chamado.getId(),

                chamado.getTitulo(),

                chamado.getDescricao(),

                chamado.getStatus(),

                chamado.getPrioridade(),

                respostaDe(chamado.getCategoria()),

                respostaDe(chamado.getUsuario()),

                respostaDe(chamado.getAdminResponsavel()),

                respostaDe(sala),

                chamado.getCriadoEm(),

                chamado.getAtualizadoEm()
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

    private CategoriaResponse respostaDe(Categoria categoria) {

        return new CategoriaResponse(

                categoria.getId(),

                categoria.getNome(),

                categoria.getDescricao()
        );
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
}