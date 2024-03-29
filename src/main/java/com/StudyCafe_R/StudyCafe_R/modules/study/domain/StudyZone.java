package com.StudyCafe_R.StudyCafe_R.modules.study.domain;

import com.StudyCafe_R.StudyCafe_R.modules.zone.Zone;
import lombok.*;

import jakarta.persistence.*;

@Entity
@Table(name = "study_zone")
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @AllArgsConstructor @NoArgsConstructor
public class StudyZone {

    @Id @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private Study study;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id")
    private Zone zone;

}