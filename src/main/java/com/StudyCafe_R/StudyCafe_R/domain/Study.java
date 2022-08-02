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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "study")
    @Builder.Default
    private Set<StudyTag> tags = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY ,mappedBy = "study")
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

    public void removeManager(AccountStudyManager accountStudyManager) {
        for (AccountStudyManager manager : managers) {
            Account account1 = manager.getAccount();
            if (doesAccountExist(account1, accountStudyManager.getAccount())) {
                managers.remove(manager);
                accountStudyManager.setStudy(null);
                break;
            }
        }
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
