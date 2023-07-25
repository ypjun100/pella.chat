package com.acapella.pella.chat.dto;

import com.acapella.pella.chat.service.ChatService;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Data
public class ChatRoom {
    private String roomId; // 채팅방 아이디
    private String name;   // 채팅방 이름
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public ChatRoom(String roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }

    public void handleAction(WebSocketSession session, ChatDTO message, ChatService service) {
        if (message.getType().equals(ChatDTO.MessageType.ENTER)) {
            sessions.add(session);

            message.setMessage(message.getSender() + " 님이 입장하셨습니다.");
            sendMessage(message, service);
        } else if (message.getType().equals(ChatDTO.MessageType.TALK)) {
            message.setMessage(message.getMessage());
            sendMessage(message, service);
        }
    }

    public <T> void sendMessage(T message, ChatService service) {
        sessions.parallelStream().forEach(session -> service.sendMessage(session, message));;
    }
}
