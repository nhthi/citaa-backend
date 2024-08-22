package com.citaa.citaa.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

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
    String content;
    @ElementCollection
    List<String> files;
    @ManyToMany
    List<Reward> rewards;
    @OneToMany
    List<User>  judges;
    @ManyToMany
    List<Startup> candidates;

}
