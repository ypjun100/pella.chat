package com.acapella.pella.chat.controller;

import com.acapella.pella.chat.dto.ChatRoom;
import com.acapella.pella.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private final ChatService service;

    @PostMapping
    public ChatRoom createRoom(@RequestParam String name) {
        return service.createRoom(name);
    }

    @GetMapping
    public List<ChatRoom> findAllRooms() {
        return service.findAllRoom();
    }
}
