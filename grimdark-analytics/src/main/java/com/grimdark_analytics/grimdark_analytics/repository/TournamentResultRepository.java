package com.grimdark_analytics.grimdark_analytics.repository;

import com.grimdark_analytics.grimdark_analytics.model.Player;
import com.grimdark_analytics.grimdark_analytics.model.Tournament;
import com.grimdark_analytics.grimdark_analytics.model.TournamentResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TournamentResultRepository extends JpaRepository<TournamentResult, Long> {

    List<TournamentResult> findByPlayer(Player player);

    List<TournamentResult> findByTournament(Tournament tournament);

    Optional<TournamentResult> findByTournamentAndPlayer(Tournament tournament, Player player);

    @Query("SELECT r.faction, SUM(r.wins), SUM(r.losses), SUM(r.draws) " +
           "FROM TournamentResult r GROUP BY r.faction ORDER BY SUM(r.wins) DESC")
    List<Object[]> aggregateByFaction();

    @Query("SELECT t.date, SUM(r.wins), SUM(r.losses), SUM(r.draws) " +
           "FROM TournamentResult r JOIN r.tournament t " +
           "WHERE r.faction = :faction " +
           "GROUP BY t.date ORDER BY t.date ASC")
    List<Object[]> aggregateByFactionAndDate(@Param("faction") String faction);
}
