package com.StudyCafe_R.StudyCafe_R.settings;

import com.StudyCafe_R.StudyCafe_R.domain.Account;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
public class Profile {

    @Length(max=35)
    private String bio;

    @Length(max=50)
    private String url;

    @Length(max=50)
    private String occupation;

    @Length(max=50)
    private String location; // varchar(255) above all info

    private String profileImage;

    public Profile(Account account) {
        this.bio = account.getBio();
        this.url = account.getUrl();
        this.occupation = account.getOccupation();
        this.location = account.getLocation();
        this.profileImage = account.getProfileImage();
    }
}
