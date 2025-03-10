package com.citaa.citaa.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
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
    @JoinColumn(name = "admin_id") // Thêm JoinColumn để xác định cột khóa ngoại
    User admin;

    @ElementCollection
    List<String> files;

    @ElementCollection
    List<String> fields = new ArrayList<>();

    @ManyToMany
    List<User> judges = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "competition_startup_time", joinColumns = @JoinColumn(name = "competition_id"))
    @MapKeyColumn(name = "startup_id") // Sử dụng ID của Startup làm khóa
    @Column(name = "applied_at")
    Map<Integer, LocalDateTime> startupAppliedTimes;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE,CascadeType.REMOVE})
    @JoinTable(
            name = "competition_project",
            joinColumns = @JoinColumn(name = "competition_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    List<Project> projects;

    @OneToMany(mappedBy = "competition", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<TimelineEvent> timelineEvents = new ArrayList<>();

    LocalDateTime createAt;
    LocalDateTime updateAt;
    LocalDate startAt;
    LocalDate endAt;

    double first = 0;
    double second=0;
    double third = 0;
    double mostVote = 0;

    boolean isRanked = false;

    @OneToMany(mappedBy = "competition", cascade = CascadeType.ALL, orphanRemoval = true)
    List<CompetitionRanking> rankings = new ArrayList<>();

    // Phương thức được gọi trước khi lưu entity mới
    @PrePersist
    protected void onCreate() {
        this.createAt = LocalDateTime.now();
    }

    // Phương thức được gọi trước khi cập nhật entity
    @PreUpdate
    protected void onUpdate() {
        this.updateAt = LocalDateTime.now();
    }
}
