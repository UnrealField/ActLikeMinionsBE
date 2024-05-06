package com.example.ActLikeMinions.domain;

public class MatchResult {
    private String userType; //Host, Client
    private String status; //OK, ERROR
    private String ipAddr;
    private int port;
    private String roomNo;

    public String getUserType() {
        return userType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }
}
