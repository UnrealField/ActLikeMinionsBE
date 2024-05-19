package com.example.ActLikeMinions.domain;

import java.util.Random;

public class RandomRoomNo {
    public String gen() {
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
}
