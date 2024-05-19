package com.example.ActLikeMinions.config;

import com.example.ActLikeMinions.handler.WebSocketChatHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final WebSocketChatHandler webSocketChatHandler;

    public WebSocketConfig(WebSocketChatHandler webSocketChatHandler) {
        this.webSocketChatHandler = webSocketChatHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(webSocketChatHandler, "/connect")
                .setAllowedOrigins("*");
    }
}
