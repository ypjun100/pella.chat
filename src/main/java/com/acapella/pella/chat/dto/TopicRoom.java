package com.acapella.pella.chat.dto;

import com.acapella.pella.chat.service.ChatService;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

@Data
public class TopicRoom extends Room {
    private int topicNumber;

    @Builder
    public TopicRoom(String id, int topicNumber) {
        this.id = id;
        this.topicNumber = topicNumber;
    }

    public void handleAction(WebSocketSession session, Chat message, ChatService service) {
        if (message.getType().equals(Chat.MessageType.ENTER)) {
            sessions.add(session);
            message.setMessage(message.getSender() + " 님이 입장하셨습니다.");
            sendMessage(message, service);
        } else if (message.getType().equals(Chat.MessageType.TALK)) {
            message.setMessage(message.getMessage());
            sendMessage(message, service);
        }
    }

    public <T> void sendMessage(T message, ChatService service) {
        sessions.parallelStream().forEach(session -> service.sendMessage(session, message));
    }
}
