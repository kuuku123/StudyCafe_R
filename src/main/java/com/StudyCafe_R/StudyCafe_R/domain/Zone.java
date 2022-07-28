package com.StudyCafe_R.StudyCafe_R.domain;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter @EqualsAndHashCode(of = {"city","localNameOfCity","province"})
@Builder @AllArgsConstructor @NoArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"city","province"}))
public class Zone {

    @Id @GeneratedValue
    @Column(name = "ZONE_ID")
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

    @Override
    public String toString() {
        return String.format("%s(%s)/%s", city, localNameOfCity, province);
    }
}