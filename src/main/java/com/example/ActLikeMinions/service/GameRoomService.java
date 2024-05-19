package com.example.ActLikeMinions.service;

import com.example.ActLikeMinions.domain.GameRoom;
import com.example.ActLikeMinions.domain.MatchResult;
import com.example.ActLikeMinions.domain.Player;
import com.example.ActLikeMinions.domain.RandomRoomNo;
import com.example.ActLikeMinions.repository.GameRoomRepository;
import com.example.ActLikeMinions.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class GameRoomService {
    private final GameRoomRepository gameRoomRepository;
    private final PlayerRepository playerRepository;

    private final RandomRoomNo rrn = new RandomRoomNo();

    @Autowired
    public GameRoomService(GameRoomRepository gameRoomRepository, PlayerRepository playerRepository) {
        this.gameRoomRepository = gameRoomRepository;
        this.playerRepository = playerRepository;
    }

    public MatchResult gameRoomRandomJoin(String ip, String port) {
        GameRoom gameRoom = gameRoomRepository.findFirstGameRoomWithMemberCountBetweenOneAndTenAndIngameN();
        if(Objects.isNull(gameRoom)) {
            //조건 만족하는 참여 가능한 방이 존재하지 않음
            //따라서 방 생성한 뒤 본인이 호스트가 됨
            //호스트 로직으로 처리
            return this.gameRoomCreate(ip, port);
        } else {
            gameRoom.increase(); //참여했으므로 방 인원 +1
            gameRoomRepository.save(gameRoom); //반영

            playerRepository.save(new Player(gameRoom.getRoomNo(),
                                            ip,
                                            port,
                                            "N"));
            //Player 정보 저장
            //조회한 Room의 RoomNo, 본인 IP, 본인 PORT, 호스트 No

            return new MatchResult("CLIENT",
                                    "OK",
                                    gameRoom.getHostIP(),
                                    gameRoom.getHostPort(),
                                    gameRoom.getRoomNo());
        }
    }

    public MatchResult gameRoomManualJoin(String roomNo, String ip, String port) {
        GameRoom room = gameRoomRepository.findByRoomNo(roomNo);
        MatchResult mr = new MatchResult();

        if(Objects.isNull(room)) {
            //일치하는 방 번호가 없는 경우
            mr.setStatus("ERROR_NO_ROOM");
            return mr;
        } else {
            //일치하는 방 번호 존재
            if(room.getMemberCount() >= 10) {
                //방 인원수 초과
                mr.setStatus("ERROR_PLAYER_LIMIT_EXCEED");
                return mr;
            } else {
                //정상 처리 로직
                room.increase(); //참여했으므로 방 인원 +1
                gameRoomRepository.save(room); //반영

                playerRepository.save(new Player(room.getRoomNo(),
                        ip,
                        port,
                        "N"));
                //Player 정보 저장
                //조회한 Room의 RoomNo, 본인 IP, 본인 PORT, 호스트 No

                return new MatchResult("CLIENT",
                        "OK",
                        room.getHostIP(),
                        room.getHostPort(),
                        room.getRoomNo());
            }
        }
    }

    public MatchResult gameRoomCreate(String ip, String port) {
        String generatedRoomNo = null;
        while(true) {
            //생성한 방 번호가 충돌이 일어날 수 있다.
            //충돌이 일어나지 않을 때 까지 반복
            generatedRoomNo = rrn.gen();
            GameRoom RandomlyAccessedRoom = gameRoomRepository.findByRoomNo(generatedRoomNo);
            if(Objects.isNull(RandomlyAccessedRoom)) {
                //가져온 GameRoom이 존재하지 않음
                //이 말은 곧 충돌이 일어나지 않은 경우
                //정상 처리후 loop 탈출
                gameRoomRepository.save(new GameRoom(generatedRoomNo,
                        ip,
                        port,
                        1,
                        "N"));
                playerRepository.save(new Player(generatedRoomNo,
                        ip,
                        port,
                        "Y"));
                break;
            }
        }
        return new MatchResult("HOST",
                                "OK",
                                ip,
                                port,
                                generatedRoomNo);
    }

    public void gameRoomCollapse() {
        gameRoomRepository.deleteAllByMemberCountZero();
    }

    public GameRoom selectGameRoom(String roomNo) {
        return gameRoomRepository.findByRoomNo(roomNo);
    }

    public void deleteGameRoom(String roomNo) {
        gameRoomRepository.deleteByRoomNo(roomNo);
    }

    public void save(GameRoom gameRoom) {
        gameRoomRepository.save(gameRoom);
    }
}
