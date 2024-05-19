package com.example.ActLikeMinions.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "gameroom")
public class GameRoom {
    @Id
    @Column(name = "roomno")
    private String roomNo;

    @Column(name = "hostip")
    private String hostIP;

    @Column(name = "hostport")
    private String hostPort;

    @Column(name = "membercount")
    private int memberCount;

    @Column(name = "ingame")
    private String isInGame;

    public void decrease() {
        this.memberCount -= 1;
    }

    public void increase() {
        this.memberCount += 1;
    }

    public GameRoom(String roomNo, String hostIP, String hostPort, int memberCount, String isInGame) {
        this.roomNo = roomNo;
        this.hostIP = hostIP;
        this.hostPort = hostPort;
        this.memberCount = memberCount;
        this.isInGame = isInGame;
    }

    public GameRoom() {
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getHostIP() {
        return hostIP;
    }

    public void setHostIP(String hostIP) {
        this.hostIP = hostIP;
    }

    public String getHostPort() {
        return hostPort;
    }

    public void setHostPort(String hostPort) {
        this.hostPort = hostPort;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public String getIsInGame() {
        return isInGame;
    }

    public void setIsInGame(String isInGame) {
        this.isInGame = isInGame;
    }
}
