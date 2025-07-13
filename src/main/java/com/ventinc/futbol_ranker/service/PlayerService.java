package com.ventinc.futbol_ranker.service;

import com.ventinc.futbol_ranker.model.Player;
import com.ventinc.futbol_ranker.repo.PlayerRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {

    private final PlayerRepo playerRepo;

    public PlayerService(PlayerRepo playerRepo) {
        this.playerRepo = playerRepo;
    }

    public List<Player> searchPlayers(String club, String name, String position, String nation, Boolean retired) {
        if (club == null || club.trim().isEmpty()) club = null;
        if (name == null || name.trim().isEmpty()) name = null;
        if (position == null || position.trim().isEmpty()) position = null;
        if (nation == null || nation.trim().isEmpty()) nation = null;

        return playerRepo.findByFilters(club, name, position, nation, retired);
    }

    public List<Player> getPlayer(){
        return playerRepo.findAll();
    }

    public Player getPlayerById(Long id){
        return playerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Player not found with id: " + id));
    }

    public void addPlayer(Player player) {
        playerRepo.save(player);
    }

    public void updatePlayer(Long id, Player updatedPlayer) {
        Optional<Player> existingPlayer = playerRepo.findById(id);

        if (existingPlayer.isPresent()){
            Player playerToUpdate = existingPlayer.get();
            playerToUpdate.setName(updatedPlayer.getName());
            playerToUpdate.setNation(updatedPlayer.getNation());
            playerToUpdate.setPosition(updatedPlayer.getPosition());
            playerToUpdate.setClub(updatedPlayer.getClub());
            playerToUpdate.setRetired(updatedPlayer.isRetired());
            playerRepo.save(playerToUpdate);
        } else {
            throw new RuntimeException("Player not found with id: " + id);
        }
    }

    @Transactional
    public void deletePlayer(Long id) {
        playerRepo.deleteById(id);
    }

    public List<Player> getPlayersFromClub(String club){
        if (club == null || club.trim().isEmpty()) {
            return List.of();
        }
        return playerRepo.findByClubContainingIgnoreCase(club);
    }

    public List<Player> getPlayersByName (String name){
        if (name == null || name.trim().isEmpty()) {
            return List.of();
        }
        return playerRepo.findByNameContainingIgnoreCase(name);
    }

    public List<Player> getPlayerByPosition (String position){
        if (position == null || position.trim().isEmpty()) {
            return List.of();
        }
        return playerRepo.findByPositionContainingIgnoreCase(position);
    }

    public List<Player> getPlayerByNation (String nation){
        if (nation == null || nation.trim().isEmpty()) {
            return List.of();
        }
        return playerRepo.findByNationContainingIgnoreCase(nation);
    }

    public List<Player> getPlayerByRetirement (Boolean retired){
        if (retired == null) {
            return List.of();
        }
        return playerRepo.findByRetired(retired);
    }
}