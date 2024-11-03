package com.citaa.citaa.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Competition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String name;
    String introduce;
    String content;
    @ManyToOne
    User admin;
    @ElementCollection
    List<String> files;
    @ElementCollection
    List<String> fields = new ArrayList<>();
    @ManyToMany
    List<Reward> rewards = new ArrayList<>();
    @ManyToMany
    List<User>  judges = new ArrayList<>();
    @ElementCollection
    @CollectionTable(name = "competition_startup_time", joinColumns = @JoinColumn(name = "competition_id"))
    @MapKeyJoinColumn(name = "startup_id")
    @Column(name = "applied_at")
    Map<Startup, LocalDateTime> startupAppliedTimes;
    @ManyToMany
    @JoinTable(
            name = "competition_project",
            joinColumns = @JoinColumn(name = "competition_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    List<Project> projects;
    @OneToMany(mappedBy = "competition", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<TimelineEvent> timelineEvents = new ArrayList<>(); // Danh sách các mốc thời gian
    LocalDateTime createAt;
    LocalDateTime updateAt;
    LocalDateTime startAt;
    LocalDateTime endAt;

}
