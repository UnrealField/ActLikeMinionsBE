package com.example.ActLikeMinions.service;

import com.example.ActLikeMinions.domain.Player;
import com.example.ActLikeMinions.repository.PlayerRepository;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Player savePlayer(Player player) {
        return playerRepository.save(player);
    }

    public void deletePlayer(String ip) {
        playerRepository.deleteByPlayerIP(ip);
    }


}
