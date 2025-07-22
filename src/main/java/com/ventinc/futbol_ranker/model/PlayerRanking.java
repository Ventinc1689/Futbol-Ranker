package com.ventinc.futbol_ranker.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "player_ranking")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerRanking {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long rankId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Column(nullable = false)
    private int ranking;
}
