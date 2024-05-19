package com.example.ActLikeMinions.handler;

import com.example.ActLikeMinions.service.GameRoomService;
import com.example.ActLikeMinions.service.PlayerService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Component
public class WebSocketChatHandler extends TextWebSocketHandler {
    private final PlayerService playerService;
    private final GameRoomService gameRoomService;

    public WebSocketChatHandler(PlayerService playerService, GameRoomService gameRoomService) {
        this.playerService = playerService;
        this.gameRoomService = gameRoomService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        try {
            System.out.println("웹소켓 연결 성공 감지");
            System.out.println("Session ID : " + session.getId());
            System.out.println("IP Addr : " + session.getRemoteAddress());
            session.sendMessage(
                    new TextMessage("웹소켓 연결성공")
            );
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws IOException {
        System.out.println("웹소켓 연결 종료 감지 : " + session.getRemoteAddress());
        playerService.deletePlayer(session.getRemoteAddress().toString());
    }
}
