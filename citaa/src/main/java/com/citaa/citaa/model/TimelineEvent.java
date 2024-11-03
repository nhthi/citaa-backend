package com.citaa.citaa.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class TimelineEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String eventName;
    String description;
    LocalDateTime eventTime;
    @ManyToOne
    @JoinColumn(name = "competition_id")
    Competition competition;
}