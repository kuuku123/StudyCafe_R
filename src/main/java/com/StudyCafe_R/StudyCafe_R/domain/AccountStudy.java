package com.StudyCafe_R.StudyCafe_R.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "account_study")
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountStudy {

    @Id @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private Study study;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;
}