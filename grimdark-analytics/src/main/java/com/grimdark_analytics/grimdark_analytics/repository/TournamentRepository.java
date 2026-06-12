package com.grimdark_analytics.grimdark_analytics.repository;

import com.grimdark_analytics.grimdark_analytics.model.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {

    List<Tournament> findByDateAfter(LocalDate date);

    List<Tournament> findByPlayerCountGreaterThanEqual(Integer minPlayers);

    Optional<Tournament> findByBcpEventId(String bcpEventId);
}
