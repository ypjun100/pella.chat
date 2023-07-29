package com.acapella.pella.chat.service;

import com.acapella.pella.chat.dto.RequestPacket;
import com.acapella.pella.chat.dto.WaitingRoom;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Data
@Service
public class WaitingRoomService {
    private final ObjectMapper mapper;
    private static WaitingRoom waitingRoom;

    public static String getWaitingRoomId() { return waitingRoom.getId(); }

    @PostConstruct
    private void init() {
        waitingRoom = new WaitingRoom(UUID.randomUUID().toString());
    }

    public void handleAction(WebSocketSession session, RequestPacket requestPacket) {
        if (requestPacket.getRoomId() != requestPacket.getRoomId())
            return;

        if (requestPacket.getType() == RequestPacket.MessageType.JOIN) {
            waitingRoom.addSession(session);
        }
    }

    public <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
