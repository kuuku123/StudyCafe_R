package com.StudyCafe_R.StudyCafe_R.domain;

import com.StudyCafe_R.StudyCafe_R.account.UserAccount;
import lombok.*;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;

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
    private Set<AccountStudy> managers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "study",cascade = CascadeType.ALL)
    @Builder.Default
    private Set<AccountStudy> members = new HashSet<>();

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

    public void addManager(AccountStudy accountStudy) {
        managers.add(accountStudy);
        accountStudy.setStudy(this);
    }

    public void removeManager(AccountStudy accountStudy) {
        for (AccountStudy manager : managers) {
            Account account1 = manager.getAccount();
            if (doesAccountExist(account1, accountStudy.getAccount())) {
                managers.remove(manager);
                accountStudy.setStudy(null);
                break;
            }
        }
    }

    public boolean isJoinable(UserAccount userAccount) {
        return this.isPublished() && this.isRecruiting() && !this.members.stream()
                .anyMatch(accountStudy -> doesAccountExist(accountStudy.getAccount(), userAccount.getAccount()));
    }

    public boolean isMember(UserAccount userAccount) {
        return this.members.stream()
                .anyMatch(accountStudy -> doesAccountExist(accountStudy.getAccount(), userAccount.getAccount()));
    }

    public boolean isManager(UserAccount userAccount) {
        return this.managers.stream()
                .anyMatch(accountStudy -> doesAccountExist(accountStudy.getAccount(), userAccount.getAccount()));
    }
    private boolean doesAccountExist(Account accountStudy, Account userAccount) {
        return accountStudy.getNickname().equals(userAccount.getNickname());
    }
}
