package com.acapella.pella.chat.handler;

import com.acapella.pella.chat.dto.RequestPacket;
import com.acapella.pella.chat.service.WaitingRoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class WaitingWebSocketHandler extends TextWebSocketHandler {
    private final ObjectMapper mapper;
    private final WaitingRoomService service;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        RequestPacket requestPacket = mapper.readValue(message.getPayload(), RequestPacket.class);
        log.info("session {}", requestPacket.toString());

        service.handleAction(session, requestPacket);
    }
}
