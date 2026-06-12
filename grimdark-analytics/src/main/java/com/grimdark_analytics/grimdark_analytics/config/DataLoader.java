package com.grimdark_analytics.grimdark_analytics.config;

import com.grimdark_analytics.grimdark_analytics.model.*;
import com.grimdark_analytics.grimdark_analytics.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@Profile("!test")
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {

    private final TournamentRepository tournamentRepo;
    private final PlayerRepository playerRepo;
    private final TournamentResultRepository resultRepo;
    private final MatchupRepository matchupRepo;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (tournamentRepo.count() > 0) {
            log.info("Database already seeded, skipping.");
            return;
        }
        log.info("Seeding database with sample W40k tournament data...");

        // ── Tournaments ──────────────────────────────────────────────────────
        Tournament londonGt = tournament("London GT 2026",        LocalDate.of(2026, 1, 18), "London, UK",      "GT", 128);
        Tournament birmOpen = tournament("Birmingham Open 2026",   LocalDate.of(2026, 2, 22), "Birmingham, UK",  "RTT", 64);
        Tournament manchInv = tournament("Manchester Invitational",LocalDate.of(2026, 3, 15), "Manchester, UK",  "GT", 96);
        Tournament bristolCl= tournament("Bristol Clash 2026",     LocalDate.of(2026, 4, 12), "Bristol, UK",     "RTT", 48);

        // ── Players ──────────────────────────────────────────────────────────
        Player alice  = player("Alice Thompson",   "North");
        Player bob    = player("Bob Harrington",   "Midlands");
        Player cara   = player("Cara Neville",     "South");
        Player dan    = player("Dan Kowalski",     "Midlands");
        Player ellie  = player("Ellie Marston",    "North");
        Player finn   = player("Finn O'Brien",     "South");
        Player grace  = player("Grace Lindqvist",  "Midlands");
        Player harry  = player("Harry Chen",       "South");
        Player ivan   = player("Ivan Petrov",      "Midlands");
        Player jasmine= player("Jasmine Park",     "North");
        Player kai    = player("Kai Rosenberg",    "South");
        Player luna   = player("Luna Ferreira",    "North");
        Player marco  = player("Marco Vitali",     "Midlands");
        Player nina   = player("Nina Sharma",      "North");
        Player oscar  = player("Oscar Dupont",     "South");
        Player priya  = player("Priya Kaur",       "Midlands");

        // ── Results: London GT (5 rounds) ────────────────────────────────────
        result(londonGt, alice,   "Aeldari",              1,  5, 0, 0);
        result(londonGt, bob,     "Tyranids",             2,  4, 1, 0);
        result(londonGt, cara,    "Necrons",              3,  4, 1, 0);
        result(londonGt, dan,     "Space Marines",        4,  3, 2, 0);
        result(londonGt, ellie,   "Chaos Space Marines",  5,  3, 2, 0);
        result(londonGt, finn,    "Death Guard",          6,  3, 2, 0);
        result(londonGt, grace,   "Aeldari",              7,  2, 3, 0);
        result(londonGt, harry,   "Orks",                 8,  2, 3, 0);
        result(londonGt, ivan,    "T'au Empire",          9,  2, 3, 0);
        result(londonGt, jasmine, "Necrons",             10,  2, 3, 0);
        result(londonGt, kai,     "Tyranids",            11,  1, 4, 0);
        result(londonGt, luna,    "Space Marines",       12,  1, 4, 0);
        result(londonGt, marco,   "World Eaters",        13,  1, 4, 0);
        result(londonGt, nina,    "Aeldari",             14,  0, 5, 0);
        result(londonGt, oscar,   "Adeptus Mechanicus",  15,  0, 5, 0);
        result(londonGt, priya,   "Astra Militarum",     16,  0, 5, 0);

        // ── Results: Birmingham Open (5 rounds) ──────────────────────────────
        result(birmOpen, alice,   "Aeldari",              1,  5, 0, 0);
        result(birmOpen, bob,     "Tyranids",             2,  4, 0, 1);
        result(birmOpen, cara,    "Necrons",              3,  4, 1, 0);
        result(birmOpen, ellie,   "Chaos Space Marines",  4,  3, 1, 1);
        result(birmOpen, finn,    "Death Guard",          5,  3, 2, 0);
        result(birmOpen, grace,   "Aeldari",              6,  3, 2, 0);
        result(birmOpen, harry,   "Orks",                 7,  2, 2, 1);
        result(birmOpen, ivan,    "T'au Empire",          8,  2, 3, 0);
        result(birmOpen, dan,     "Space Marines",        9,  2, 3, 0);
        result(birmOpen, jasmine, "Necrons",             10,  1, 3, 1);
        result(birmOpen, kai,     "Tyranids",            11,  1, 4, 0);
        result(birmOpen, luna,    "Space Marines",       12,  0, 4, 1);

        // ── Results: Manchester Invitational (5 rounds) ──────────────────────
        result(manchInv, bob,     "Tyranids",             1,  5, 0, 0);
        result(manchInv, alice,   "Aeldari",              2,  4, 1, 0);
        result(manchInv, cara,    "Necrons",              3,  4, 1, 0);
        result(manchInv, finn,    "Death Guard",          4,  3, 2, 0);
        result(manchInv, ellie,   "Chaos Space Marines",  5,  3, 2, 0);
        result(manchInv, dan,     "Space Marines",        6,  3, 2, 0);
        result(manchInv, marco,   "World Eaters",         7,  3, 2, 0);
        result(manchInv, grace,   "Aeldari",              8,  2, 3, 0);
        result(manchInv, priya,   "Astra Militarum",      9,  2, 3, 0);
        result(manchInv, oscar,   "Adeptus Mechanicus",  10,  1, 4, 0);
        result(manchInv, nina,    "Aeldari",             11,  1, 4, 0);
        result(manchInv, harry,   "Orks",                12,  0, 5, 0);

        // ── Results: Bristol Clash (5 rounds) ────────────────────────────────
        result(bristolCl, cara,    "Necrons",              1,  5, 0, 0);
        result(bristolCl, ellie,   "Chaos Space Marines",  2,  4, 1, 0);
        result(bristolCl, alice,   "Aeldari",              3,  4, 1, 0);
        result(bristolCl, dan,     "Space Marines",        4,  3, 2, 0);
        result(bristolCl, bob,     "Tyranids",             5,  3, 2, 0);
        result(bristolCl, priya,   "Astra Militarum",      6,  3, 2, 0);
        result(bristolCl, finn,    "Death Guard",          7,  2, 3, 0);
        result(bristolCl, ivan,    "T'au Empire",          8,  2, 3, 0);
        result(bristolCl, marco,   "World Eaters",         9,  1, 4, 0);
        result(bristolCl, oscar,   "Adeptus Mechanicus",  10,  0, 5, 0);

        // ── Matchups: London GT round 1 ──────────────────────────────────────
        matchup(londonGt, 1, alice, "Aeldari",             bob,    "Tyranids",            alice);
        matchup(londonGt, 1, cara,  "Necrons",             dan,    "Space Marines",       cara);
        matchup(londonGt, 1, ellie, "Chaos Space Marines", finn,   "Death Guard",         ellie);
        matchup(londonGt, 1, grace, "Aeldari",             harry,  "Orks",                grace);
        matchup(londonGt, 1, ivan,  "T'au Empire",         jasmine,"Necrons",             ivan);
        matchup(londonGt, 1, kai,   "Tyranids",            luna,   "Space Marines",       luna);
        matchup(londonGt, 1, marco, "World Eaters",        nina,   "Aeldari",             marco);
        matchup(londonGt, 1, oscar, "Adeptus Mechanicus",  priya,  "Astra Militarum",     null); // draw

        // London GT round 2
        matchup(londonGt, 2, alice, "Aeldari",             cara,   "Necrons",             alice);
        matchup(londonGt, 2, bob,   "Tyranids",            ellie,  "Chaos Space Marines", bob);
        matchup(londonGt, 2, dan,   "Space Marines",       finn,   "Death Guard",         dan);
        matchup(londonGt, 2, grace, "Aeldari",             ivan,   "T'au Empire",         ivan);
        matchup(londonGt, 2, harry, "Orks",                jasmine,"Necrons",             harry);
        matchup(londonGt, 2, kai,   "Tyranids",            marco,  "World Eaters",        marco);
        matchup(londonGt, 2, luna,  "Space Marines",       nina,   "Aeldari",             luna);
        matchup(londonGt, 2, oscar, "Adeptus Mechanicus",  priya,  "Astra Militarum",     null);

        // London GT round 3
        matchup(londonGt, 3, alice, "Aeldari",             dan,    "Space Marines",       alice);
        matchup(londonGt, 3, bob,   "Tyranids",            cara,   "Necrons",             bob);
        matchup(londonGt, 3, ellie, "Chaos Space Marines", grace,  "Aeldari",             ellie);
        matchup(londonGt, 3, finn,  "Death Guard",         harry,  "Orks",                finn);
        matchup(londonGt, 3, ivan,  "T'au Empire",         luna,   "Space Marines",       ivan);
        matchup(londonGt, 3, jasmine,"Necrons",            kai,    "Tyranids",            jasmine);
        matchup(londonGt, 3, marco, "World Eaters",        oscar,  "Adeptus Mechanicus",  marco);
        matchup(londonGt, 3, nina,  "Aeldari",             priya,  "Astra Militarum",     priya);

        // Birmingham Open round 1
        matchup(birmOpen, 1, alice, "Aeldari",             cara,   "Necrons",             alice);
        matchup(birmOpen, 1, bob,   "Tyranids",            dan,    "Space Marines",       bob);
        matchup(birmOpen, 1, ellie, "Chaos Space Marines", finn,   "Death Guard",         ellie);
        matchup(birmOpen, 1, grace, "Aeldari",             harry,  "Orks",                harry);
        matchup(birmOpen, 1, ivan,  "T'au Empire",         jasmine,"Necrons",             ivan);
        matchup(birmOpen, 1, kai,   "Tyranids",            luna,   "Space Marines",       null); // draw

        // Manchester Invitational round 1
        matchup(manchInv, 1, bob,   "Tyranids",            alice,  "Aeldari",             bob);
        matchup(manchInv, 1, cara,  "Necrons",             ellie,  "Chaos Space Marines", cara);
        matchup(manchInv, 1, finn,  "Death Guard",         dan,    "Space Marines",       finn);
        matchup(manchInv, 1, marco, "World Eaters",        grace,  "Aeldari",             marco);
        matchup(manchInv, 1, priya, "Astra Militarum",     oscar,  "Adeptus Mechanicus",  priya);
        matchup(manchInv, 1, nina,  "Aeldari",             harry,  "Orks",                nina);

        // Bristol Clash round 1
        matchup(bristolCl,1, cara,  "Necrons",             alice,  "Aeldari",             cara);
        matchup(bristolCl,1, ellie, "Chaos Space Marines", bob,    "Tyranids",            ellie);
        matchup(bristolCl,1, dan,   "Space Marines",       finn,   "Death Guard",         dan);
        matchup(bristolCl,1, priya, "Astra Militarum",     marco,  "World Eaters",        priya);
        matchup(bristolCl,1, ivan,  "T'au Empire",         oscar,  "Adeptus Mechanicus",  ivan);

        log.info("Seed complete. {} tournaments, {} players, {} results, {} matchups.",
                tournamentRepo.count(), playerRepo.count(), resultRepo.count(), matchupRepo.count());
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Tournament tournament(String name, LocalDate date, String location, String format, int players) {
        Tournament t = new Tournament();
        t.setName(name); t.setDate(date); t.setLocation(location);
        t.setFormat(format); t.setPlayerCount(players);
        return tournamentRepo.save(t);
    }

    private Player player(String name, String region) {
        Player p = new Player();
        p.setName(name); p.setRegion(region);
        return playerRepo.save(p);
    }

    private void result(Tournament t, Player p, String faction, int placement, int w, int l, int d) {
        TournamentResult r = new TournamentResult();
        r.setTournament(t); r.setPlayer(p); r.setFaction(faction);
        r.setPlacement(placement); r.setWins(w); r.setLosses(l); r.setDraws(d);
        resultRepo.save(r);
    }

    private void matchup(Tournament t, int round,
                         Player p1, String f1,
                         Player p2, String f2,
                         Player winner) {
        Matchup m = new Matchup();
        m.setTournament(t); m.setRound(round);
        m.setPlayer1(p1); m.setPlayer2(p2); m.setWinner(winner);
        m.setPlayer1Faction(f1); m.setPlayer2Faction(f2);
        matchupRepo.save(m);
    }
}
