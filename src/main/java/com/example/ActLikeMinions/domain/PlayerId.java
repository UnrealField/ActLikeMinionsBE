package com.example.ActLikeMinions.domain;

import java.io.Serializable;

public class PlayerId implements Serializable {
    private String roomNo;
    private String playerIP;

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
}
