package com.citaa.citaa.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EvaluationCompetition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne
    @JoinColumn(name = "judge_id", nullable = false)
    User judge; // Giám khảo (User trong hệ thống)
    int projectId;
    int competitionId;
    int points;
    @Lob
    @Column(columnDefinition = "TEXT")
    String content;
    @Lob
    @Column(columnDefinition = "TEXT")
    String comment;

    LocalDateTime createAt;
}
