package com.grimdark_analytics.grimdark_analytics.repository;

import com.grimdark_analytics.grimdark_analytics.model.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {

    List<Unit> findByFaction(String faction);
}
