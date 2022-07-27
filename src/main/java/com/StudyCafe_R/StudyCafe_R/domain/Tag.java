package com.StudyCafe_R.StudyCafe_R.domain;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
@EqualsAndHashCode(of="id")
@Builder @AllArgsConstructor @NoArgsConstructor
public class Tag {

    @Id @GeneratedValue
    @Column(name = "TAG_ID")
    private Long id;

    @Column(unique = true, nullable = false)
    private String title;

    @OneToMany(mappedBy = "tag",cascade = CascadeType.ALL)
    private Set<AccountTag> accountTagSet = new HashSet<>();

}
