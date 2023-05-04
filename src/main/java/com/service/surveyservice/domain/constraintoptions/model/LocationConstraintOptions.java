package com.service.surveyservice.domain.constraintoptions.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LocationConstraintOptions {
    @Id
    @Column(name = "locationConstraintOptions_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Float latitude;

    @Column
    private Float longitude;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "constraintOptions_id")
    private ConstraintOptions constraintOptions;
}

