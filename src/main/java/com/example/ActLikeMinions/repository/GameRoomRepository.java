package com.example.ActLikeMinions.repository;

import com.example.ActLikeMinions.domain.GameRoom;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRoomRepository extends JpaRepository<GameRoom, String> {
    @Query("SELECT g FROM GameRoom g WHERE g.memberCount > 0 AND g.memberCount < 10 AND g.isInGame = 'N' ORDER BY g.memberCount ASC LIMIT 1")
    GameRoom findFirstGameRoomWithMemberCountBetweenOneAndTenAndIngameN();

    GameRoom findByRoomNo(String roomNo);

    @Modifying
    @Transactional
    @Query("DELETE FROM GameRoom g WHERE g.memberCount = 0")
    void deleteAllByMemberCountZero();

}
