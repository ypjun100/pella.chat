package com.acapella.pella.chat.service;

import com.acapella.pella.chat.dto.ChatRoom;
import com.acapella.pella.chat.dto.RequestPacket;
import com.acapella.pella.chat.dto.ResponsePacket;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@Slf4j
@Data
@Service
public class WChatService {
    @Value("${application.number_of_topics}")
    private int numberOfTopics;

    private final ObjectMapper mapper;
    private ArrayList<LinkedList<WebSocketSession>> waitingRooms;
    private ArrayList<Map<String, ChatRoom>> chatRooms;

    @PostConstruct
    private void init() {
        waitingRooms = new ArrayList<>();
        for (int i = 0; i < numberOfTopics; i++)
            waitingRooms.add(new LinkedList<>());

        chatRooms = new ArrayList<>();
        for (int i = 0; i < numberOfTopics; i++)
            chatRooms.add(new HashMap<>());
    }

    public void join(WebSocketSession session, RequestPacket requestPacket) {
        int topicId = requestPacket.getTopicId();
        if (!waitingRooms.get(topicId).isEmpty()) { // 해당 토픽의 대기방에 인원이 있는경우
            // 세션이 연결된 사용자를 찾음
            WebSocketSession existingSession = waitingRooms.get(topicId).get(0); // 임시로 유저 한 명을 넣음
            for (int i = 0; i < waitingRooms.get(topicId).size(); i++) {
                if (waitingRooms.get(topicId).get(0).isOpen()) {
                    existingSession = waitingRooms.get(topicId).pop();
                    break;
                }
                waitingRooms.get(topicId).pop();
            }

            String chatRoomId = UUID.randomUUID().toString();
            ChatRoom room = ChatRoom.builder()
                    .roomId(chatRoomId)
                    .topicId(topicId).build();

            ResponsePacket message = ResponsePacket.builder()
                    .type(ResponsePacket.MessageType.ENTER)
                    .roomId(chatRoomId)
                    .topicId(topicId)
                    .build();
            sendMessage(session, message);
            sendMessage(existingSession, message);

            chatRooms.get(topicId).put(chatRoomId, room); // 채팅방 생성
        } else { // 인원이 없는 경우
            waitingRooms.get(topicId).add(session);
        }
    }

    public void enter(WebSocketSession session, RequestPacket requestPacket) {
        int topicId = requestPacket.getTopicId();
        String roomId = requestPacket.getRoomId();

        if (chatRooms.get(topicId).containsKey(roomId)) {
            chatRooms.get(topicId).get(roomId).addSession(session, this);
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
