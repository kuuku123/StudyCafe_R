package com.StudyCafe_R.StudyCafe_R.settings;

import com.StudyCafe_R.StudyCafe_R.domain.Account;
import lombok.Data;

@Data
public class Profile {

    private String bio;

    private String url;

    private String occupation;

    private String location; // varchar(255) above all info

    public Profile(Account account) {
        this.bio = account.getBio();
        this.url = account.getUrl();
        this.occupation = account.getOccupation();
        this.location = account.getLocation();
    }
}
