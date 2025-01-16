package com.citaa.citaa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class CompetitionRanking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "competition_id", nullable = false)
    Competition competition;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    Project project;

    @Column(name = "ranking")
    int rank=100; // Vị trí xếp hạng: 1, 2, 3, v.v.

    double score; // Điểm của startup

    int vote=0;

    boolean isVotest = false;
}