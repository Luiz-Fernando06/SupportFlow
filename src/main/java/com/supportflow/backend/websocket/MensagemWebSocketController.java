package com.supportflow.backend.websocket;

import com.supportflow.backend.service.MensagemService;
import com.supportflow.backend.dto.request.EnviarMensagemRequest;
import com.supportflow.backend.dto.response.MensagemResponse;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class MensagemWebSocketController {

    private final MensagemService mensagemService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/salas/{salaId}/enviar")
    public void enviarMensagem(@DestinationVariable Long salaId, EnviarMensagemRequest request) {

        MensagemResponse resposta = mensagemService.enviar(salaId, request);

        messagingTemplate.convertAndSend("/topic/salas/" + salaId, resposta);

    }

}