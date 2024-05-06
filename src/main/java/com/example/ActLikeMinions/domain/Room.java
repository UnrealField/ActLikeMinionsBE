package com.example.ActLikeMinions.domain;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private String roomNo;
    private String hostIp;
    private int hostPort;
    public List<String> ipPort;
    public void hostMigrate() {
        String[] sliced = ipPort.get(0).split(":");
        this.hostIp = sliced[0];
        this.hostPort = Integer.parseInt(sliced[1]);
        System.out.println("hostMigrate()");
    }

    public Boolean disconnect(String ip) {
        try {
            int idx = 0;
            for(int i = 0 ; i < this.ipPort.size() ; i++) {
                if(ipPort.get(i).equals(ip)) {
                    idx = i;
                    System.out.println("get(i) : " + ipPort.get(i));
                    break;
                }
            }

            System.out.println("idx : " + idx);

            if(idx == 0) {
                //Host disconnected
                if(ipPort.size() == 1) {
                    ipPort.clear();
                    System.out.println("Disconnect (Host)");
                    return true;
                }
                else {
                    ipPort.remove(0);
                    hostMigrate();
                    System.out.println("Disconnect (Host)");
                    return true;
                }
            }
            else {
                //Client disconnected
                ipPort.remove(idx);
                System.out.println("Disconnect (Client)");
                return true;
            }
        } catch (Exception e) {
            System.out.println("Unexpected error on Disconnect()");
            e.printStackTrace();
            return false;
        }
    }

    public Room(String roomNo, String hostIp, int hostPort) {
        this.roomNo = roomNo;
        this.hostIp = hostIp;
        this.hostPort = hostPort;
        this.ipPort = new ArrayList<>();
        ipPort.add(hostIp + ":" + hostPort);
        System.out.println("Room generated : " + roomNo);
        System.out.println("Size : " + this.ipPort.size());
        System.out.println("Appended : " + this.ipPort.get(0));
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getHostIp() {
        return hostIp;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    public int getHostPort() {
        return hostPort;
    }

    public void setHostPort(int hostPort) {
        this.hostPort = hostPort;
    }
}
