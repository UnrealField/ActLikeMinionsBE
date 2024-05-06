package com.example.ActLikeMinions.controller;

import com.example.ActLikeMinions.domain.ConnectionStatus;
import com.example.ActLikeMinions.domain.MatchResult;
import com.example.ActLikeMinions.domain.Room;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api")
public class RoomController {
    private static ConcurrentHashMap<String, Room> roomList = new ConcurrentHashMap<>();

    @Scheduled(fixedDelay = 10000)
    public void roomSync() {
        for(String key : roomList.keySet()) {
            if(roomList.get(key).ipPort.isEmpty()) {
                roomList.remove(key);
                //빈 방은 제거
                System.out.println("roomSync() " + key);
            }
        }
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

    public String roomNumberGenerator() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String numbers = "0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for(int i = 0 ; i < 4 ; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        for(int i = 0 ; i < 4 ; i++) {
            sb.append(numbers.charAt(random.nextInt(numbers.length())));
        }
        return sb.toString();
    }


    @PostMapping("/host")
    public MatchResult hostRoom(HttpServletRequest req) {
        MatchResult mr = new MatchResult();
        String ip = ip(req);
        int port = req.getRemotePort();

        //Generating roomNo
        Boolean isDistinct = false;
        String roomNo;

        while(!isDistinct) {
            roomNo = roomNumberGenerator();
            if(roomList.containsKey(roomNo)) {
                //Redundant;
            }
            else {
                Room room = new Room(roomNo, ip, port);
                roomList.put(roomNo, room);
                mr.setRoomNo(roomNo);
                mr.setIpAddr(ip);
                mr.setPort(port);
                mr.setUserType("Host");
                mr.setStatus("OK");
                isDistinct = true;
            }
        }
        System.out.println("Host() : " + mr.getRoomNo());
        return mr;
    }

    @PostMapping("/join/{roomNo}")
    public MatchResult joinRoomWithRoomNo(@PathVariable String roomNo, HttpServletRequest req) {
        String ip = ip(req);
        int port = req.getRemotePort();
        MatchResult mr = new MatchResult();
        String ipPort = ip + ":" + port;

        mr.setRoomNo(roomNo);
        mr.setUserType("Client");

        if(roomList.containsKey(roomNo)) {
            if(roomList.get(roomNo).ipPort.size() >= 10) {
                mr.setStatus("FULL");
                System.out.println("join(roomNo) : FULL");
            }
            else {
                mr.setStatus("OK");
                mr.setIpAddr(roomList.get(roomNo).getHostIp());
                mr.setPort(roomList.get(roomNo).getHostPort());
                roomList.get(roomNo).ipPort.add(ipPort);
                System.out.println("join(roomNo) : " + roomNo);
            }
        }
        else {
            mr.setStatus("ERROR");
            System.out.println("join(roomNo) : ERROR");
        }
        return mr;
    }

    @PostMapping("/join")
    public MatchResult joinRoomAsRandom(HttpServletRequest req) {
        String ip = ip(req);
        int port = req.getRemotePort();
        String ipPort = ip + ":" + port;
        MatchResult mr = new MatchResult();

        if(roomList.isEmpty()) {
            //No available room, be host;
            String roomNo = roomNumberGenerator();
            mr.setStatus("OK");
            mr.setUserType("Host");
            mr.setIpAddr(ip);
            mr.setPort(port);
            mr.setRoomNo(roomNo);
            Room room = new Room(roomNo, ip, port);
            roomList.put(roomNo, room);
            System.out.println("join() : Host");
        }
        else {
            //Be client
            boolean isLoopedAll = true;
            for(Room room : roomList.values()) {
                if(room.ipPort.size() < 10) {
                    //join
                    isLoopedAll = false;
                    String roomNo = room.getRoomNo();
                    mr.setStatus("OK");
                    mr.setUserType("Client");
                    mr.setIpAddr(roomList.get(roomNo).getHostIp());
                    mr.setPort(roomList.get(roomNo).getHostPort());
                    mr.setRoomNo(roomNo);
                    roomList.get(roomNo).ipPort.add(ipPort);
                    System.out.println("join() : Client : " + roomNo);
                    System.out.println("Room count : " + roomList.get(roomNo).ipPort.size());
                    break;
                }
                else {
                    //Search next room
                }
            }

            if(isLoopedAll) {
                //No available room; be host
                Boolean isDistinct = false;
                while(!isDistinct) {
                    String roomNo = roomNumberGenerator();
                    if(roomList.containsKey(roomNo)) {
                        //Redundant;
                    }
                    else {
                        Room room = new Room(roomNo, ip, port);
                        roomList.put(roomNo, room);
                        mr.setRoomNo(roomNo);
                        mr.setIpAddr(ip);
                        mr.setPort(port);
                        mr.setUserType("Host");
                        mr.setStatus("OK");
                        isDistinct = true;
                    }
                }
                System.out.println("join() : Host (No empty lobby)");
            }
            return mr;
        }
        return mr;
    }


    @PostMapping("/disconnect/{roomNo}")
    public ConnectionStatus disconnect(@PathVariable String roomNo, HttpServletRequest req) {
        String ip = ip(req);
        ConnectionStatus cs = new ConnectionStatus();
        Boolean isDisconnected = roomList.get(roomNo).disconnect(ip);
        if(isDisconnected) {
            cs.setDisconnect("OK");
        }
        else {
            cs.setDisconnect("ERROR");
        }
        System.out.println("size : " + roomList.get(roomNo).ipPort.size());
        return cs;
    }


    /*@GetMapping("/ip")
    public String ipPort(HttpServletRequest req) {
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


        int port = req.getRemotePort();
        System.out.println(ip + ":" + port);
        System.out.println(ip);

        return ip + ":" + port;
    }*/
}