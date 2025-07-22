package com.ventinc.futbol_ranker.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PlayerRankingDTO {

    @NotNull(message = "Player ID is required")
    private long playerId;

    @NotNull(message = "Ranking is required")
    @Min(value = 1, message = "Ranking must be at least 1-25")
    @Max(value = 25, message = "Ranking must be at most 25")
    private int ranking;
}
