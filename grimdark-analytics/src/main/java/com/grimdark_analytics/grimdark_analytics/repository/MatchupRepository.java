package com.grimdark_analytics.grimdark_analytics.repository;

import com.grimdark_analytics.grimdark_analytics.model.Matchup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchupRepository extends JpaRepository<Matchup, Long> {

    @Query("SELECT m FROM Matchup m WHERE " +
           "(m.player1Faction = :f1 AND m.player2Faction = :f2) OR " +
           "(m.player1Faction = :f2 AND m.player2Faction = :f1)")
    List<Matchup> findByFactions(@Param("f1") String faction1, @Param("f2") String faction2);
}
