package com.ventinc.futbol_ranker.service;

import com.ventinc.futbol_ranker.DTO.PlayerRankingDTO;
import com.ventinc.futbol_ranker.DTO.PlayerRankingResponseDTO;
import com.ventinc.futbol_ranker.model.Player;
import com.ventinc.futbol_ranker.model.PlayerRanking;
import com.ventinc.futbol_ranker.model.Users;
import com.ventinc.futbol_ranker.repo.PlayerRankingRepo;
import com.ventinc.futbol_ranker.repo.PlayerRepo;
import com.ventinc.futbol_ranker.repo.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PlayerRankingService {

    private final PlayerRankingRepo playerRankingRepo;
    private final PlayerRepo playerRepo;
    private final UserRepo userRepo;

    public PlayerRankingService(PlayerRankingRepo playerRankingRepo, PlayerRepo playerRepo, UserRepo userRepo) {
        this.playerRankingRepo = playerRankingRepo;
        this.playerRepo = playerRepo;
        this.userRepo = userRepo;
    }

    public PlayerRankingResponseDTO rankPlayer(Long userId, PlayerRankingDTO rankingDTO){
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Player player = playerRepo.findById(rankingDTO.getPlayerId())
                .orElseThrow(() -> new RuntimeException("Player not found"));

        Optional<PlayerRanking> existingRanking = playerRankingRepo.findByUserUserIdAndPlayerId(userId, rankingDTO.getPlayerId());

        PlayerRanking ranking;

        if(existingRanking.isPresent()) {
            ranking = existingRanking.get();
            ranking.setRanking(rankingDTO.getRanking());
        } else {
            ranking = new PlayerRanking();
            ranking.setUser(user);
            ranking.setPlayer(player);
            ranking.setRanking(rankingDTO.getRanking());
        }

        PlayerRanking saveRanking = playerRankingRepo.save(ranking);
        return mapToResponseDTO(saveRanking);
    }

    public List<PlayerRankingResponseDTO> getUserRankings(Long userId){
        return playerRankingRepo.findByUserUserId(userId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<PlayerRankingResponseDTO> getPlayerRankings(Long playerId){
        return playerRankingRepo.findByPlayerId(playerId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public void deleteRanking(Long userId, Long playerId){
        Optional<PlayerRanking> ranking = playerRankingRepo.findByUserUserIdAndPlayerId(userId, playerId);
        if (ranking.isPresent()) {
            playerRankingRepo.delete(ranking.get());
        } else {
            throw new RuntimeException("Ranking not found for userId: " + userId + " and playerId: " + playerId);
        }
    }

    private PlayerRankingResponseDTO mapToResponseDTO(PlayerRanking ranking){
        PlayerRankingResponseDTO dto = new PlayerRankingResponseDTO();
        dto.setRankId(ranking.getRankId());
        dto.setPlayerId(ranking.getPlayer().getId());
        dto.setPlayerName(ranking.getPlayer().getName());
        dto.setUserId(ranking.getUser().getUserId());
        dto.setUserName(ranking.getUser().getUsername());
        dto.setRanking(ranking.getRanking());
        return dto;
    }
}
