package com.ventinc.futbol_ranker.controller;

import com.ventinc.futbol_ranker.DTO.PlayerRankingDTO;
import com.ventinc.futbol_ranker.DTO.PlayerRankingResponseDTO;
import com.ventinc.futbol_ranker.service.PlayerRankingService;
import com.ventinc.futbol_ranker.service.UsersService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rankings")
public class PlayerRankingController {

    PlayerRankingService playerRankingService;
    UsersService usersService;

    public PlayerRankingController(PlayerRankingService playerRankingService, UsersService usersService) {
        this.playerRankingService = playerRankingService;
        this.usersService = usersService;
    }

    @PostMapping
    public ResponseEntity<PlayerRankingResponseDTO> rankPlayer(@Valid @RequestBody PlayerRankingDTO rankingDTO) {
        String username = getCurrentUserName();
        Long userId = usersService.getUserIdByUsername(username);
        PlayerRankingResponseDTO response = playerRankingService.rankPlayer(userId, rankingDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-rankings")
    public ResponseEntity<List<PlayerRankingResponseDTO>> getMyRankings(){
        String username = getCurrentUserName();
        Long userId = usersService.getUserIdByUsername(username);
        return ResponseEntity.ok(playerRankingService.getUserRankings(userId));
    }

    @GetMapping("/player/{playerId}")
    public ResponseEntity<List<PlayerRankingResponseDTO>> getPlayerRankings(@PathVariable Long playerId){
        return ResponseEntity.ok(playerRankingService.getPlayerRankings(playerId));
    }

    @DeleteMapping("/player/{playerId}")
    public ResponseEntity<Void> deletePlayerRanking(@PathVariable Long playerId){
        String username = getCurrentUserName();
        Long userId = usersService.getUserIdByUsername(username);
        playerRankingService.deleteRanking(userId, playerId);
        return ResponseEntity.noContent().build();
    }

    private String getCurrentUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

}
