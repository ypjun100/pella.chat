package com.acapella.pella.chat.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponsePacket {
    public enum MessageType { ENTER, DATA, TALK }

    private ResponsePacket.MessageType type; // 메시지 타입
    private String roomId;    // 방 번호
    private int topicId;   // 토픽 주제 ID
    private String sender;    // 채팅을 보낸 사람
    private String message;   // 메시지 내용
    private String time;      // 채팅 발송 시간
}
