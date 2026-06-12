package com.grimdark_analytics.grimdark_analytics.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "tournaments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String bcpEventId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate date;

    private String location;

    private String format;

    private Integer playerCount;
}
