package com.StudyCafe_R.StudyCafe_R.modules.study;

import com.StudyCafe_R.StudyCafe_R.infra.MockMvcTest;
import com.StudyCafe_R.StudyCafe_R.modules.account.AccountFactory;
import com.StudyCafe_R.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.StudyCafe_R.modules.account.form.SignUpForm;
import com.StudyCafe_R.StudyCafe_R.modules.account.repository.AccountRepository;
import com.StudyCafe_R.StudyCafe_R.modules.account.service.AccountService;
import com.StudyCafe_R.StudyCafe_R.modules.study.domain.Study;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
class StudySettingControllerTest {
    @Autowired MockMvc mockMvc;
    @Autowired StudyFactory studyFactory;
    @Autowired AccountFactory accountFactory;
    @Autowired AccountRepository accountRepository;
    @Autowired StudyRepository studyRepository;
    @Autowired
    AccountService accountService;

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

    @Test
    @WithUserDetails(value = "tony",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("스터디 소개 수정 폼 form 조회 - 실패 (권한 없는 유저)")
    void updateDescriptionForm_fail() throws Exception {
        Account tony_member = accountFactory.createAccount("tony-test");
        Study study = studyFactory.createStudy("test-study", tony_member);

        mockMvc.perform(get("/study/"+study.getEncodedPath() + "/settings/description"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "tony",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("스터디 소개 수정 폼 form 조회 - 성공")
    void updateDescriptionForm_success() throws Exception {
        Account managerTony = accountRepository.findByNickname("tony");
        Study study = studyFactory.createStudy("test-study", managerTony);

        mockMvc.perform(get("/study/" + study.getEncodedPath() + "/settings/description"))
                .andExpect(status().isOk())
                .andExpect(view().name("study/settings/description"));
    }

    @Test
    @WithUserDetails(value = "tony",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("스터디 소개 수정 - 실패")
    void updateDescription_fail() throws Exception {
        Account tony = accountRepository.findByNickname("tony");
        Study study = studyFactory.createStudy("test-study", tony);

        String settingsDescriptionUrl = "/study/" + study.getEncodedPath() + "/settings/description";
        mockMvc.perform(post(settingsDescriptionUrl)
                .param("shortDescription","")
                .param("fullDescription","full description")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("studyDescriptionForm"))
                .andExpect(model().attributeExists("study"))
                .andExpect(model().attributeExists("account"));
    }

    @Test
    @WithUserDetails(value = "tony",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("스터디 소개 수정 - 성공")
    void updateDescription_success() throws Exception {
        Account tony = accountRepository.findByNickname("tony");
        Study study = studyFactory.createStudy("test-study", tony);

        String settingsDescriptionUrl = "/study/" + study.getEncodedPath() + "/settings/description";
        mockMvc.perform(post(settingsDescriptionUrl)
                        .param("shortDescription","short description")
                        .param("fullDescription","full description")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(settingsDescriptionUrl))
                .andExpect(flash().attributeExists("message"));
    }


}