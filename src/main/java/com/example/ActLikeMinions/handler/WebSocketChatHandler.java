package com.example.ActLikeMinions.handler;

import com.example.ActLikeMinions.domain.GameRoom;
import com.example.ActLikeMinions.domain.Player;
import com.example.ActLikeMinions.service.GameRoomService;
import com.example.ActLikeMinions.service.PlayerService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

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
            String ip = null;
            int colonIndex = session.getRemoteAddress().toString().indexOf(':');
            if (colonIndex > 1) {
                ip = session.getRemoteAddress().toString().substring(1, colonIndex);
            }
            System.out.println(ip);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws IOException {
        GameRoom gameroom = new GameRoom();
        Player player = new Player();

        //IP 파싱
        String ip = null;
        int colonIndex = session.getRemoteAddress().toString().indexOf(':');
        if (colonIndex > 1) {
            ip = session.getRemoteAddress().toString().substring(1, colonIndex);
        }

        player = playerService.selectPlayer(ip);
        gameroom = gameRoomService.selectGameRoom(player.getRoomNo());

        if(player.getIsHost().equals("Y")) {
            //접속을 종료한 플레이어가 특정 방의 호스트
            //해당 로비는 제거됨. 해당 방의 모든 플레이어 제거
            gameRoomService.deleteGameRoom(player.getRoomNo()); //방 제거
            playerService.collapsePlayer(player.getRoomNo());   //방에 있는 모든플레이어
        } else {
            //접속 종료한 플레이어가 호스트가 아닌 경우
            //방 인원 한 명 감소시킨 뒤 해당 플레이어만 제거
            gameroom.decrease();
            gameRoomService.save(gameroom);
            playerService.deletePlayer(ip);
        }
    }
}
