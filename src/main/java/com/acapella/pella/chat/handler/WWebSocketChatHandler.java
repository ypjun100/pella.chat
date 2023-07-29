package com.acapella.pella.chat.handler;

import com.acapella.pella.chat.dto.RequestPacket;
import com.acapella.pella.chat.service.WChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Primary
@Component
@RequiredArgsConstructor
public class WWebSocketChatHandler extends TextWebSocketHandler {
    private final ObjectMapper mapper;
    private final WChatService service;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        RequestPacket requestPacket = mapper.readValue(message.getPayload(), RequestPacket.class);

        if (requestPacket.getType() == RequestPacket.MessageType.JOIN) {
            service.join(session, requestPacket);
        } else if (requestPacket.getType() == RequestPacket.MessageType.ENTER) {
            service.enter(session, requestPacket);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {

    }
}
