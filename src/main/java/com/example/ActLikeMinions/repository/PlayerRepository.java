package com.example.ActLikeMinions.repository;

import com.example.ActLikeMinions.domain.Player;
import com.example.ActLikeMinions.domain.PlayerId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, PlayerId> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Player p WHERE p.playerIP = :playerIP")
    void deleteByPlayerIP(String playerIP);
    Player save(Player player);

    @Modifying
    @Transactional
    @Query("DELETE FROM Player p WHERE p.roomNo = :roomNo")
    void deleteByRoomNo(String roomNo);

    Player findByPlayerIP(String playerIP);
}
