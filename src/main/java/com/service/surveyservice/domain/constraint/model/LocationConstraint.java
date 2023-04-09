package com.service.surveyservice.domain.constraint.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LocationConstraint {
    @Id
    @Column(name = "locationConstraint_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Float latitude;

    @Column
    private Float longitude;

    @OneToOne
    @JoinColumn(name = "constraint_id")
    private Constraint constraint;
}

