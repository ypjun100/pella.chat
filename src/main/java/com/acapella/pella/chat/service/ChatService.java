package com.acapella.pella.chat.service;

import com.acapella.pella.chat.component.TopicProperty;
import com.acapella.pella.chat.dto.ChatRoom;
import com.acapella.pella.chat.dto.RequestPacket;
import com.acapella.pella.chat.dto.ResponsePacket;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@Slf4j
@AllArgsConstructor
@Service
public class ChatService {
    private final TopicProperty topicProperty; // 토픽 프로퍼티를 가져온 클래스

    private final ObjectMapper mapper;

    private ArrayList<LinkedList<WebSocketSession>> waitingUsers;
    // 대기유저가 LinkedList인 이유는 FIFO 순서성을 가져야 하기 때문

    private ArrayList<Map<String, ChatRoom>> chatRooms; // 실제 유저들이 대화를 나눌 채팅방

    @PostConstruct // 클래스 내에서 의존성 주입을 마치면 실행
    public void init() {
        // 대기유저 리스트 초기화
        waitingUsers = new ArrayList<>();
        for (int i = 0; i < topicProperty.getNumberOfTopics(); i++)
            waitingUsers.add(new LinkedList<>());

        // 채팅방 리스트 초기화
        chatRooms = new ArrayList<>();
        for (int i = 0; i < topicProperty.getNumberOfTopics(); i++)
            chatRooms.add(new HashMap<>());
    }

    // 사용자 대기방 입장
    public void join(WebSocketSession session, RequestPacket requestPacket) {
        int topicId = requestPacket.getTopicId(); // 현재 사용자의 토픽 ID
        // 세션이 끊긴 대기유저의 세션 삭제
        for (int i = 0; i < waitingUsers.get(topicId).size(); i++) {
            if (waitingUsers.get(topicId).get(0).isOpen())
                break;
            waitingUsers.get(topicId).pop();
        }

        if (!waitingUsers.get(topicId).isEmpty()) { // 해당 토픽의 대기유저 있는경우
            String chatRoomId = UUID.randomUUID().toString();
            ChatRoom room = ChatRoom.builder()
                    .roomId(chatRoomId)
                    .topicId(topicId).build(); // 새로운 채팅방 생성

            ResponsePacket message = ResponsePacket.builder()
                    .type(ResponsePacket.MessageType.DATA)
                    .roomId(chatRoomId)
                    .topicId(topicId)
                    .build(); // 생성된 채팅방의 ID를 포함한 응답패킷 생성
            sendMessage(session, message); // 현재 사용자에게 패킷 전달
            sendMessage(waitingUsers.get(topicId).pop(), message); // 기존에 대기유저에게 패킷 전달 (패킷을 전달함과 동시에 대기 유저 pop)

            chatRooms.get(topicId).put(chatRoomId, room); // 채팅방 생성
        } else { // 인원이 없는 경우
            waitingUsers.get(topicId).add(session); // 대기유저에 세션 추가
        }
    }

    // 사용자 채팅방 입장
    public void enter(WebSocketSession session, RequestPacket requestPacket) {
        int topicId = requestPacket.getTopicId(); // 토픽 ID
        String roomId = requestPacket.getRoomId(); // 채팅방 ID

        if (chatRooms.get(topicId).containsKey(roomId)) { // 토픽, 채팅방 ID가 모두 있는 경우 해당 사용자 세션을 채팅방에 추가
            chatRooms.get(topicId).get(roomId).addSession(session, this, topicProperty.getTopic(topicId).getTopic());
        }
    }

    // 사용자 간 대화
    public void talk(RequestPacket requestPacket) {
        int topicId = requestPacket.getTopicId(); // 토픽 ID
        String roomId = requestPacket.getRoomId(); // 채팅방 ID

        if (chatRooms.get(topicId).containsKey(roomId)) { // 토픽, 채팅방 ID가 모두 있는 경우 메시지 전달
            // 메시지 전달은 보낸 유저와 받는 유저 모두에게 전달함
            // 보낸 유저에게 메시지를 다시 반환함으로써 정상적으로 전달됨을 전달
            ResponsePacket message = ResponsePacket.builder()
                    .type(ResponsePacket.MessageType.TALK)
                    .roomId(roomId)
                    .topicId(topicId)
                    .message(requestPacket.getMessage())
                    .sender(requestPacket.getSender())
                    .build();
            chatRooms.get(topicId).get(roomId).sendMessage(message, this);
        }
    }

    // 특정 세션에게 메시지 전달
    public <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
