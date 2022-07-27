package com.StudyCafe_R.StudyCafe_R.account.service;

import com.StudyCafe_R.StudyCafe_R.account.SignUpForm;
import com.StudyCafe_R.StudyCafe_R.account.UserAccount;
import com.StudyCafe_R.StudyCafe_R.account.repository.AccountRepository;
import com.StudyCafe_R.StudyCafe_R.domain.Account;
import com.StudyCafe_R.StudyCafe_R.domain.AccountTag;
import com.StudyCafe_R.StudyCafe_R.domain.Tag;
import com.StudyCafe_R.StudyCafe_R.settings.form.Notifications;
import com.StudyCafe_R.StudyCafe_R.settings.form.Profile;
import com.StudyCafe_R.StudyCafe_R.tag.TagRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@RequiredArgsConstructor
@Service
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final TagRepository tagRepository;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;


    public Account processNewAccount(SignUpForm signUpForm) {

        Account newAccount = saveNewAccount(signUpForm);
        newAccount.generateEmailCheckToken();
        sendSignupConfirmEmail(newAccount);
        return newAccount;
    }
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

    public void sendSignupConfirmEmail(Account newAccount) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(newAccount.getEmail());
        simpleMailMessage.setSubject("스터디카페, 회원 가입 인증");
        simpleMailMessage.setText("/check-email-token?token=" + newAccount.getEmailCheckToken() + "&email=" + newAccount.getEmail());

        javaMailSender.send(simpleMailMessage);
    }

    //TODO password Authentication 이 정석적인 방법이 아니라 혼란을 야기할 수 있다.
    public void login(Account account) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new UserAccount(account),account.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);
    }

    public void completeSignUp(Account account) {
        account.completeSignUp();
        login(account);
    }

    public void updateProfile(Account account, Profile profile) {
        modelMapper.map(profile,account);
        accountRepository.save(account); // merge detached entity
    }

    public void updatePassword(Account account, String newPassword) {
        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
    }

    public void updateNotifications(Account account, Notifications notifications) {
        modelMapper.map(notifications,account);
        accountRepository.save(account);
    }

    public void updateNickname(Account account, String nickname) {
        account.setNickname(nickname);
        accountRepository.save(account);
        login(account);
    }

    public void sendLoginLink(Account account) {
        account.generateEmailCheckToken();
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(account.getEmail());
        mailMessage.setSubject("스터디올래, 로그인 링크");
        mailMessage.setText("/login-by-email?token=" + account.getEmailCheckToken() +
                "&email=" + account.getEmail());
        javaMailSender.send(mailMessage);
    }

    public void addTag(Account account, Tag tag) {
        AccountTag accountTag = AccountTag.builder()
                .tag(tag)
                .build();
        accountRepository.findById(account.getId())
                .ifPresent(a -> a.addAccountTag(accountTag));
    }

    public Set<AccountTag> getTags(Account account) {
        Optional<Account> byId = accountRepository.findById(account.getId());
        return byId.orElseThrow().getAccountTagSet();
    }

    public void removeTag(Account account, Tag tag) {

        accountRepository.findById(account.getId())
                .ifPresent(a -> a.removeAccountTag(tag));

//        tagRepository.delete(tag);  // we want tag to be alive , later we can look AccountTag table and search for Tag that has no reference
    }
}
