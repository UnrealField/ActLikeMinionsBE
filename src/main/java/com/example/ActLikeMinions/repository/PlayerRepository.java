package com.example.ActLikeMinions.repository;

import com.example.ActLikeMinions.domain.Player;
import com.example.ActLikeMinions.domain.PlayerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, PlayerId> {
    void deleteByPlayerIP(String playerIP);
    Player save(Player player);

    void deleteByRoomNo(String roomNo);


}
