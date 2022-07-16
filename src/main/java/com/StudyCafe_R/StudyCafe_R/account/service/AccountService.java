package com.StudyCafe_R.StudyCafe_R.account.service;

import com.StudyCafe_R.StudyCafe_R.account.SignUpForm;
import com.StudyCafe_R.StudyCafe_R.account.repository.AccountRepository;
import com.StudyCafe_R.StudyCafe_R.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;

    private Account saveNewAccount(SignUpForm signUpForm) {
        Account account = Account.builder()
                .email(signUpForm.getEmail())
                .nickname(signUpForm.getNickname())
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .studyCreatedByWeb(true)
                .studyEnrollmentResultByWeb(true)
                .studyUpdatedByWeb(true)
                .build();
        return accountRepository.save(account);
    }

    private void sendSignupConfirmEmail(Account newAccount) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(newAccount.getEmail());
        simpleMailMessage.setSubject("스터디카페, 회원 가입 인증");
        simpleMailMessage.setText("/check-email-token?token=" + newAccount.getEmailCheckToken() + "&email=" + newAccount.getEmail());

        javaMailSender.send(simpleMailMessage);
    }

    @Transactional
    public Account processNewAccount(SignUpForm signUpForm) {

        Account newAccount = saveNewAccount(signUpForm);
        newAccount.generateEmailCheckToken();
        sendSignupConfirmEmail(newAccount);
        return newAccount;
    }
}
