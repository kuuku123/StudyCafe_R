package com.StudyCafe_R.StudyCafe_R.settings;

import com.StudyCafe_R.StudyCafe_R.account.SignUpForm;
import com.StudyCafe_R.StudyCafe_R.account.repository.AccountRepository;
import com.StudyCafe_R.StudyCafe_R.account.service.AccountService;
import com.StudyCafe_R.StudyCafe_R.domain.Account;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SettingsControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    AccountService accountService;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @BeforeEach
    void beforeEach() {
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setNickname("tony");
        signUpForm.setEmail("tony@email.com");
        signUpForm.setPassword("12345678");
        accountService.processNewAccount(signUpForm);
    }
    @AfterEach
    void afterEach() {
        accountRepository.deleteAll();
    }


    @WithUserDetails(value = "tony",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("닉네임 수정 폼")
    @Test
    void updateAccountForm() throws Exception {
        mockMvc.perform(get(SettingsController.SETTINGS_ACCOUNT_URL))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("nicknameForm"));
    }

    @WithUserDetails(value = "tony",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("닉네임 수정하기 - 입력값 정상")
    @Test
    void updateAccount_success() throws Exception {
        String newNickname = "whiteship";
        mockMvc.perform(post(SettingsController.SETTINGS_ACCOUNT_URL)
                        .param("nickname", newNickname)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(SettingsController.SETTINGS_ACCOUNT_URL))
                .andExpect(flash().attributeExists("message"));

        assertNotNull(accountRepository.findByNickname("whiteship"));
    }

    @WithUserDetails(value = "tony",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("닉네임 수정하기 - 입력값 에러")
    @Test
    void updateAccount_failure() throws Exception {
        String newNickname = "¯\\_(ツ)_/¯";
        mockMvc.perform(post(SettingsController.SETTINGS_ACCOUNT_URL)
                        .param("nickname", newNickname)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(SettingsController.SETTINGS_ACCOUNT_VIEW_NAME))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("nicknameForm"));
    }
    @WithUserDetails(value = "tony",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("프로필 수정 폼 요청")
    @Test
    void updateProfileForm() throws Exception {
        String bio = "짧은 소개를 수정하는 경우";
        mockMvc.perform(get(SettingsController.SETTINGS_PROFILE_URL))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profile"));
    }


    @WithUserDetails(value = "tony",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("프로필 수정 하기 - 입력값 정상")
    @Test
    void updateProfile() throws Exception {
        String bio = "짧은 소개를 수정하는 경우";
        mockMvc.perform(post(SettingsController.SETTINGS_PROFILE_URL)
                .param("bio",  bio)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(SettingsController.SETTINGS_PROFILE_URL))
                .andExpect(flash().attributeExists("message"));

        Account tony = accountRepository.findByNickname("tony");
        assertEquals(bio,tony.getBio());
    }

    @WithUserDetails(value = "tony",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("프로필 수정 하기 - 입력값 에러")
    @Test
    void updateProfile_error() throws Exception {
        String bio = "짧은짧은 소개를 수정하는 경우짧은 소개를 수정하는 경우짧은 소개를 수정하는 경우짧은 소개를 수정하는 경우짧은 소개를 수정하는 경우 소개를 수정하는 경우";
        mockMvc.perform(post(SettingsController.SETTINGS_PROFILE_URL)
                        .param("bio",  bio)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(SettingsController.SETTINGS_PROFILE_VIEW_NAME))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profile"))
                .andExpect(model().hasErrors());

        Account tony = accountRepository.findByNickname("tony");
        assertNotEquals(bio,tony.getBio());
    }

    @WithUserDetails(value = "tony",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("패스워드 수정 폼")
    @Test
    void updatePassword_form() throws Exception {
        mockMvc.perform(get(SettingsController.SETTINGS_PASSWORD_URL))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("passwordForm"));
    }

    @WithUserDetails(value = "tony",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("패스워드 수정 - 입력값 정상")
    @Test
    void updatePassword_success() throws Exception {
        mockMvc.perform(post(SettingsController.SETTINGS_PASSWORD_URL)
                        .param("newPassword", "12345678")
                        .param("newPasswordConfirm", "12345678")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(SettingsController.SETTINGS_PASSWORD_URL))
                .andExpect(flash().attributeExists("message"));

        Account keesun = accountRepository.findByNickname("keesun");
        assertTrue(passwordEncoder.matches("12345678", keesun.getPassword()));
    }

    @WithUserDetails(value = "tony",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("패스워드 수정 - 입력값 에러 - 패스워드 불일치")
    @Test
    void updatePassword_fail() throws Exception {
        mockMvc.perform(post(SettingsController.SETTINGS_PASSWORD_URL)
                        .param("newPassword", "12345678")
                        .param("newPasswordConfirm", "11111111")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(SettingsController.SETTINGS_PASSWORD_VIEW_NAME))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("passwordForm"))
                .andExpect(model().attributeExists("account"));
    }
}
