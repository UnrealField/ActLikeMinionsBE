package com.example.ActLikeMinions.controller;

import com.example.ActLikeMinions.domain.*;
import com.example.ActLikeMinions.service.GameRoomService;
import com.example.ActLikeMinions.service.PlayerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api")
public class RoomController {

    private final GameRoomService gameRoomService;
    private final PlayerService playerService;


    @Autowired
    public RoomController(GameRoomService gameRoomService, PlayerService playerService) {
        this.gameRoomService = gameRoomService;
        this.playerService = playerService;
    }

    @Scheduled(fixedDelay = 10000)
    public void roomSync() {
        gameRoomService.gameRoomCollapse();
        System.out.println("Room Collapsed");
    }

    @PostMapping("/host")
    public MatchResult hostRoom(HttpServletRequest req) {
        String ip = ip(req);
        System.out.println("ip : " + ip);
        String port = Integer.toString(req.getRemotePort());
        return gameRoomService.gameRoomCreate(ip, port);
    }

    @PostMapping("/join/{roomNo}")
    public MatchResult joinRoomWithRoomNo(@PathVariable String roomNo, HttpServletRequest req) {
        String ip = ip(req);
        String port = Integer.toString(req.getRemotePort());
        return gameRoomService.gameRoomManualJoin(roomNo, ip, port);
    }

    @PostMapping("/join")
    public MatchResult joinRoomAsRandom(HttpServletRequest req) {
        String ip = ip(req);
        String port = Integer.toString(req.getRemotePort());
        return gameRoomService.gameRoomRandomJoin(ip, port);
    }

    @PostMapping("/change")
    public MatchResult modifyPlayingState(HttpServletRequest req) {
        String ip = ip(req);
        String port = Integer.toString(req.getRemotePort());
        return gameRoomService.update(ip);
    }

    public String ip(HttpServletRequest req) {
        String ip = req.getHeader("X-Forwarded-For");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = req.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = req.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = req.getHeader("HTTP_CLIENT_IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = req.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = req.getRemoteAddr();
        }
        return ip;
    }
}