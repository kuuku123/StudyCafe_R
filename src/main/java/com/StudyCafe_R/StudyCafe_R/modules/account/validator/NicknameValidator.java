package com.StudyCafe_R.StudyCafe_R.modules.account.validator;

import com.StudyCafe_R.StudyCafe_R.modules.account.AccountRepository;
import com.StudyCafe_R.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.StudyCafe_R.modules.account.form.NicknameForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class NicknameValidator implements Validator {

    private final AccountRepository accountRepository;
    @Override
    public boolean supports(Class<?> clazz) {
        return NicknameForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        NicknameForm nicknameForm = (NicknameForm) target;
        Account byNickname = accountRepository.findByNickname(nicknameForm.getNickname());
        if (byNickname != null) {
            errors.rejectValue("nickname","wrong.value","입력하신 닉네임을 사용할 수 없습니다.");
        }
    }
}
