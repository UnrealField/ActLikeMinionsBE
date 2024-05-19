package com.example.ActLikeMinions.domain;

public class MatchResult {
    private String userType; //Host, Client
    private String status; //OK, ERROR
    private String ipAddr;
    private String port;
    private String roomNo;

    public MatchResult(String userType, String status, String ipAddr, String port, String roomNo) {
        this.userType = userType;
        this.status = status;
        this.ipAddr = ipAddr;
        this.port = port;
        this.roomNo = roomNo;
    }

    public MatchResult() {
    }

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

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }
}
