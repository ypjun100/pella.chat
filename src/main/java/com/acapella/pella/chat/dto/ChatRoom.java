package com.acapella.pella.chat.dto;

import com.acapella.pella.chat.service.ChatService;
import com.acapella.pella.chat.service.WChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


@Data
public class ChatRoom {
    private String roomId; // 채팅방 아이디
    private String name;   // 채팅방 이름
    private int topicId; // 토픽 ID
    private Set<WebSocketSession> sessions = new HashSet<>();
    private final ObjectMapper mapper = new ObjectMapper();

    @Builder
    public ChatRoom(String roomId, int topicId) {
        this.roomId = roomId;
        this.topicId = topicId;
    }

    public void addSession(WebSocketSession session, WChatService service) {
        sessions.add(session);

        ResponsePacket message = ResponsePacket.builder()
                        .type(ResponsePacket.MessageType.DATA)
                        .roomId(this.roomId)
                        .topicId(this.topicId)
                        .message("님이 입장하셨습니다.")
                        .build();
        service.sendMessage(session, message);
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
        sessions.parallelStream().forEach(session -> service.sendMessage(session, message));
    }

    public <T> void sendMessage(T message, WChatService service) {
        sessions.parallelStream().forEach(session -> service.sendMessage(session, message));
    }
}
