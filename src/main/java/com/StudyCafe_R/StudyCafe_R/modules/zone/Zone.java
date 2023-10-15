package com.StudyCafe_R.StudyCafe_R.modules.zone;

import com.StudyCafe_R.StudyCafe_R.modules.account.domain.AccountZone;
import com.StudyCafe_R.StudyCafe_R.modules.study.domain.StudyZone;
import lombok.*;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter @EqualsAndHashCode(of = {"city","localNameOfCity","province"})
@Builder @AllArgsConstructor @NoArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"city","province"}))
public class Zone {

    @Id @GeneratedValue
    @Column(name = "zone_id")
    private Long id;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String localNameOfCity;

    @Column(nullable = true)
    private String province;

    @OneToMany(mappedBy = "zone",cascade = CascadeType.ALL)
    @Builder.Default
    private Set<AccountZone> accountZoneSet = new HashSet<>();

    @OneToMany(mappedBy = "zone", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<StudyZone> studyZoneSet = new HashSet<>();

    @Override
    public String toString() {
        return String.format("%s(%s)/%s", city, localNameOfCity, province);
    }
}
