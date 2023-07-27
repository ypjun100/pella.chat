package com.acapella.pella.chat.dto;

import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Data
public class Room {
    protected String id;
    protected Set<WebSocketSession> sessions = new HashSet<>();
}
