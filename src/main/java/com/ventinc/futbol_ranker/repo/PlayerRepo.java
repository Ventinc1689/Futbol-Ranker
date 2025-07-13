package com.ventinc.futbol_ranker.repo;

import com.ventinc.futbol_ranker.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PlayerRepo extends JpaRepository<Player, Long> {
    // Custom query methods for efficient filtering
    List<Player> findByClubContainingIgnoreCase(String club);
    List<Player> findByNameContainingIgnoreCase(String name);
    List<Player> findByPositionContainingIgnoreCase(String position);
    List<Player> findByNationContainingIgnoreCase(String nation);
    List<Player> findByRetired(Boolean retired);

    @Query(value = "SELECT * FROM player_info p WHERE " +
            "(:club IS NULL OR p.club ILIKE '%' || CAST(:club AS VARCHAR) || '%') AND " +
            "(:name IS NULL OR p.name ILIKE '%' || CAST(:name AS VARCHAR) || '%') AND " +
            "(:position IS NULL OR p.position ILIKE '%' || CAST(:position AS VARCHAR) || '%') AND " +
            "(:nation IS NULL OR p.nation ILIKE '%' || CAST(:nation AS VARCHAR) || '%') AND " +
            "(:retired IS NULL OR p.retired = :retired)",
            nativeQuery = true)
    List<Player> findByFilters(@Param("club") String club,
                               @Param("name") String name,
                               @Param("position") String position,
                               @Param("nation") String nation,
                               @Param("retired") Boolean retired);
}
