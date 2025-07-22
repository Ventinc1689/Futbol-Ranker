package com.ventinc.futbol_ranker.DTO;

import lombok.Data;

@Data
public class PlayerRankingResponseDTO {
    private long rankId;
    private long playerId;
    private String playerName;
    private long userId;
    private String userName;
    private int ranking;
}
