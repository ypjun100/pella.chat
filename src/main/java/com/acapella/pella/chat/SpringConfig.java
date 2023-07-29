package com.acapella.pella.chat;

import com.acapella.pella.chat.handler.WWebSocketChatHandler;
import com.acapella.pella.chat.handler.WaitingWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class SpringConfig implements WebSocketConfigurer {
    private final WebSocketHandler webSocketHandler;
    private final WWebSocketChatHandler wWebSocketChatHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/ws/chat").setAllowedOrigins("*");
        registry.addHandler(wWebSocketChatHandler, "/ws").setAllowedOrigins("*");
    }
}
