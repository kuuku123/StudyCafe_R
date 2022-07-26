package com.StudyCafe_R.StudyCafe_R.domain;

import javax.persistence.*;

@Entity
public class AccountTag {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name="ACCOUNT_ID")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "TAG_ID")
    private Tag tag;
}
