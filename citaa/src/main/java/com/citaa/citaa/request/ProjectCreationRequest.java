package com.citaa.citaa.request;


import com.citaa.citaa.model.Founder;
import jakarta.persistence.Lob;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectCreationRequest {

    String name;
    double realTotalCapital;
    String field;
    List<String> files;
    @Lob
    String introduce;
    @Lob
    String startUpIdea;
    @Lob
    String formationProject;
    String email;
    String phone;
    List<Founder> founders;
    String address;
    String linkWeb;
    String businessModel;
    String mainTechnology;
    String pitchDeck;
    String businessRegistrationCertificate;
    String patent;
    @Lob
    String greenSustainableElement;
}
