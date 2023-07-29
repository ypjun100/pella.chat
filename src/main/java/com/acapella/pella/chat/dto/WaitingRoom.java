package com.acapella.pella.chat.dto;

import com.acapella.pella.chat.service.WaitingRoomService;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

@Data
public class WaitingRoom extends Room {
    @Builder
    public WaitingRoom(String id) {
        this.id = id;
    }

    public void addSession(WebSocketSession session) {
        sessions.add(session);
        System.out.println(sessions);
    }

    public void handleAction(WebSocketSession session, RequestPacket message, WaitingRoomService service) {
        if (message.getType().equals(RequestPacket.MessageType.JOIN)) {
            sessions.add(session);
            message.setMessage("OK");
            service.sendMessage(session, message);
        }
    }
}
