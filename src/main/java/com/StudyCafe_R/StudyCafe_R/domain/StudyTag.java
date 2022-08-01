package com.StudyCafe_R.StudyCafe_R.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "study_tag")
@Getter @Setter @EqualsAndHashCode(of = "id")
public class StudyTag {

    @Id @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "study_id")
    private Study study;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;

}