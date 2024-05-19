package com.example.ActLikeMinions.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "player")
@IdClass(PlayerId.class)
public class Player {
    @Id
    @Column(name = "roomno")
    private String roomNo;

    @Id
    @Column(name = "playerip")
    private String playerIP;

    @Column(name = "playerport")
    private String playerPort;

    @Column(name = "ishost")
    private String isHost;

    public Player(String roomNo, String playerIP, String playerPort, String isHost) {
        this.roomNo = roomNo;
        this.playerIP = playerIP;
        this.playerPort = playerPort;
        this.isHost = isHost;
    }

    public Player() {
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getPlayerIP() {
        return playerIP;
    }

    public void setPlayerIP(String playerIP) {
        this.playerIP = playerIP;
    }

    public String getPlayerPort() {
        return playerPort;
    }

    public void setPlayerPort(String playerPort) {
        this.playerPort = playerPort;
    }

    public String getIsHost() {
        return isHost;
    }

    public void setIsHost(String isHost) {
        this.isHost = isHost;
    }
}
