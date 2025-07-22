package com.ventinc.futbol_ranker.repo;

import com.ventinc.futbol_ranker.model.PlayerRanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRankingRepo extends JpaRepository<PlayerRanking, Long> {

    Optional<PlayerRanking> findByUserUserIdAndPlayerId(long userId, long playerId);
    List<PlayerRanking> findByUserUserId(long userId);
    List<PlayerRanking> findByPlayerId(long playerId);

}
