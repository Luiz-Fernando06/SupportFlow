package com.supportflow.backend.service;

import com.supportflow.backend.dto.request.EnviarMensagemRequest;
import com.supportflow.backend.dto.response.MensagemResponse;
import com.supportflow.backend.dto.response.UsuarioResponse;
import com.supportflow.backend.enums.Role;
import com.supportflow.backend.exception.RegraDeNegocioException;
import com.supportflow.backend.exception.UsuarioNaoEncontradoException;
import com.supportflow.backend.model.Chamado;
import com.supportflow.backend.model.Mensagem;
import com.supportflow.backend.model.SalaDeAtendimento;
import com.supportflow.backend.model.Usuario;
import com.supportflow.backend.repository.MensagemRepository;
import com.supportflow.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MensagemService {

    private final MensagemRepository mensagemRepository;
    private final UsuarioRepository usuarioRepository;
    private final SalaDeAtendimentoService salaDeAtendimentoService;

    public MensagemResponse enviar(
            Long salaId,
            EnviarMensagemRequest request
    ) {

        SalaDeAtendimento sala = salaDeAtendimentoService.buscarPorId(salaId);

        if (!sala.isAtiva()) {
            throw new RegraDeNegocioException(
                    "A sala de atendimento está encerrada."
            );
        }

        Usuario usuario = usuarioLogado();

        validarParticipante(sala, usuario);

        Mensagem mensagem = new Mensagem(
                sala,
                usuario,
                request.conteudo().trim()
        );

        mensagemRepository.save(mensagem);

        return respostaDe(mensagem);
    }

    public List<MensagemResponse> listar(
            Long salaId) {

        SalaDeAtendimento sala =
                salaDeAtendimentoService.buscarPorId(salaId);

        Usuario usuario = usuarioLogado();

        validarParticipante(sala, usuario);

        List<Mensagem> mensagens =
                mensagemRepository.findBySala(sala);

        List<MensagemResponse> respostas =
                new ArrayList<>();

        for (Mensagem mensagem : mensagens) {
            respostas.add(respostaDe(mensagem));
        }

        return respostas;
    }

    private void validarParticipante(
            SalaDeAtendimento sala,
            Usuario usuario) {

        Chamado chamado = sala.getChamado();

        if (usuario.getRole() == Role.ADMIN) {
            return;
        }

        if (!chamado.getUsuario().getId().equals(usuario.getId())) {
            throw new RegraDeNegocioException(
                    "Você não participa deste atendimento."
            );
        }
    }

    private Usuario usuarioLogado() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return usuarioRepository
                .findUsuarioByEmail(email)
                .orElseThrow(() ->
                        new UsuarioNaoEncontradoException(
                                "Usuário não encontrado."
                        ));
    }

    private MensagemResponse respostaDe(Mensagem mensagem) {

        return new MensagemResponse(
                mensagem.getId(),
                respostaDe(mensagem.getRemetente()),
                mensagem.getConteudo(),
                mensagem.getEnviadaEm()
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