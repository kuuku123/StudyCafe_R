package com.StudyCafe_R.StudyCafe_R.modules.account.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Data
public class NicknameForm {

    @NotBlank
    @Length(min=3, max=20)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9_-]{3,20}$")
    private String nickname;
}
