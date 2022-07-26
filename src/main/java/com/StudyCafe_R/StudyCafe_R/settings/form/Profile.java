package com.StudyCafe_R.StudyCafe_R.settings.form;

import com.StudyCafe_R.StudyCafe_R.domain.Account;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
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
}
