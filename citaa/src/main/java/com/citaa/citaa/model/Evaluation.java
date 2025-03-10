package com.citaa.citaa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.util.QTypeContributor;

import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    int points;
    @Lob
    @Column(columnDefinition = "TEXT")
    String content;
    @Lob
    @Column(columnDefinition = "TEXT")
    String comment;
    LocalDateTime createAt;
    int projectId;
   @ManyToOne
   User expert;
}
