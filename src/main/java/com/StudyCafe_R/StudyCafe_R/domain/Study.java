package com.StudyCafe_R.StudyCafe_R.domain;

import com.StudyCafe_R.StudyCafe_R.account.UserAccount;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@NamedEntityGraph(name = "Study.withAll", attributeNodes = {
        @NamedAttributeNode("tags"),
        @NamedAttributeNode("zones"),
        @NamedAttributeNode("managers"),
        @NamedAttributeNode("members")
})
@NamedEntityGraph(name = "Study.withTagsAndManagers",attributeNodes = {
        @NamedAttributeNode("tags"),
        @NamedAttributeNode("managers")
})
@NamedEntityGraph(name = "Study.withZonesAndManagers",attributeNodes = {
        @NamedAttributeNode("zones"),
        @NamedAttributeNode("managers")
})

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @AllArgsConstructor @NoArgsConstructor
public class Study {

    @Id @GeneratedValue
    @Column(name = "study_id")
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "study",cascade = CascadeType.ALL)
    @Builder.Default
    private Set<AccountStudyManager> managers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "study",cascade = CascadeType.ALL)
    @Builder.Default
    private Set<AccountStudyMembers> members = new HashSet<>();

    @Column(unique = true)
    private String path;

    private String title;

    private String shortDescription;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String fullDescription;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String image;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "study",cascade = CascadeType.ALL)
    @Builder.Default
    private Set<StudyTag> tags = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY ,mappedBy = "study",cascade = CascadeType.ALL)
    @Builder.Default
    private Set<StudyZone> zones = new HashSet<>();

    private LocalDateTime publishedDateTime;

    private LocalDateTime closedDateTime;

    private LocalDateTime recruitingUpdateDateTime;

    private boolean recruiting;

    private boolean published;

    private boolean closed;

    private boolean useBanner;

    public void addManager(AccountStudyManager accountStudyManager) {
        managers.add(accountStudyManager);
        accountStudyManager.setStudy(this);
    }
//TODO check if i need to set accountStudyManager study to null
    public void removeManager(AccountStudyManager accountStudyManager) {
        managers.removeIf(asm -> asm.getAccount() == accountStudyManager.getAccount());
//        accountStudyManager.setStudy(null);
    }

    public void addMember(AccountStudyMembers accountStudyMembers) {
        members.add(accountStudyMembers);
        accountStudyMembers.setStudy(this);
    }

    public void removeMember(AccountStudyMembers accountStudyMembers) {
        members.removeIf(asm -> asm.getAccount() == accountStudyMembers.getAccount());
//        accountStudyMembers.setStudy(null);
    }

    public void addStudyTag(StudyTag studyTag) {
        this.tags.add(studyTag);
        studyTag.setStudy(this);
    }

    public void removeStudyTag(Tag tag) {
        tags.stream()
                        .filter(st -> st.getTag() == tag)
                                .findAny().ifPresent(st -> st.setStudy(null));
        tags.removeIf(st -> st.getTag() == tag);
    }

    public void addStudyZone(StudyZone studyZone) {
        this.zones.add(studyZone);
        studyZone.setStudy(this);
    }

    public void removeStudyZone(Zone zone) {
        zones.stream()
                        .filter(sz -> sz.getZone() == zone)
                                .findAny().ifPresent(sz -> sz.setStudy(null));
        zones.removeIf(sz -> sz.getZone() == zone);
    }

    public String getImage() {
        return image != null ? image : "/images/1.jpg";
    }

    public boolean isJoinable(UserAccount userAccount) {
        return this.isPublished() && this.isRecruiting() && !this.members.stream()
                .anyMatch(accountStudyManager -> doesAccountExist(accountStudyManager.getAccount(), userAccount.getAccount()));
    }

    public boolean isMember(UserAccount userAccount) {
        return this.members.stream()
                .anyMatch(accountStudyManager -> doesAccountExist(accountStudyManager.getAccount(), userAccount.getAccount()));
    }

    public boolean isManager(UserAccount userAccount) {
        return this.managers.stream()
                .anyMatch(accountStudyManager -> doesAccountExist(accountStudyManager.getAccount(), userAccount.getAccount()));
    }
    private boolean doesAccountExist(Account accountStudy, Account userAccount) {
        return accountStudy.getNickname().equals(userAccount.getNickname());
    }
}
