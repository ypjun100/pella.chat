package com.acapella.pella.chat.dto;

import com.acapella.pella.chat.service.ChatService;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

@Data
public class WaitingRoom extends Room {
    @Builder
    public WaitingRoom(String id) {
        this.id = id;
    }

    public void handleAction(WebSocketSession session, Chat message, ChatService service) {
        if (message.getType().equals(Chat.MessageType.JOIN)) {
            sessions.add(session);
        }
    }
}
