package com.ventinc.futbol_ranker.controller;

import com.ventinc.futbol_ranker.model.Player;
import com.ventinc.futbol_ranker.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/player")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping
    public ResponseEntity<List<Player>> getAllPlayers() {
        return ResponseEntity.ok(playerService.getPlayer());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Player>> searchPlayers(
        @RequestParam(required = false) String club,
        @RequestParam(required = false) String nation,
        @RequestParam(required = false) String position,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) Boolean retired) {

        return ResponseEntity.ok(playerService.searchPlayers(club, name, position, nation, retired));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable Long id) {
        return ResponseEntity.ok(playerService.getPlayerById(id));
    }

    @GetMapping("/club/{club}")
    public ResponseEntity<List<Player>> getPlayersByClub(@PathVariable String club) {
        return ResponseEntity.ok(playerService.getPlayersFromClub(club));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<Player>> getPlayersByName(@PathVariable String name) {
        return ResponseEntity.ok(playerService.getPlayersByName(name));
    }

    @GetMapping("/position/{position}")
    public ResponseEntity<List<Player>> getPlayersByPosition(@PathVariable String position) {
        return ResponseEntity.ok(playerService.getPlayerByPosition(position));
    }

    @GetMapping("/nation/{nation}")
    public ResponseEntity<List<Player>> getPlayersByNation(@PathVariable String nation) {
        return ResponseEntity.ok(playerService.getPlayerByNation(nation));
    }

    @GetMapping("/retired/{retired}")
    public ResponseEntity<List<Player>> getPlayersByRetirement(@PathVariable Boolean retired) {
        return ResponseEntity.ok(playerService.getPlayerByRetirement(retired));
    }

    @PostMapping
    public ResponseEntity<Void> addPlayer(@Valid @RequestBody Player player) {
        playerService.addPlayer(player);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePlayer(@PathVariable Long id, @Valid @RequestBody Player player) {
        playerService.updatePlayer(id, player); // Pass both id and player
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {
        playerService.deletePlayer(id);
        return ResponseEntity.noContent().build();
    }
}
