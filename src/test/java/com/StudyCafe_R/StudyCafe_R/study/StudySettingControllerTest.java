package com.StudyCafe_R.StudyCafe_R.study;

import com.StudyCafe_R.StudyCafe_R.domain.Account;
import com.StudyCafe_R.StudyCafe_R.domain.Study;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
class StudySettingControllerTest extends StudyControllerTest{

    @Test
    @WithUserDetails(value = "tony",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("스터디 소개 수정 폼 form 조회 - 실패 (권한 없는 유저)")
    void updateDescriptionForm_fail() throws Exception {
        Account tony_member = createAccount("tony-test");
        Study study = createStudy("test-study", tony_member);

        mockMvc.perform(get("/study/"+study.getEncodedPath() + "/settings/description"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "tony",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("스터디 소개 수정 폼 form 조회 - 성공")
    void updateDescriptionForm_success() throws Exception {
        Account managerTony = accountRepository.findByNickname("tony");
        Study study = createStudy("test-study", managerTony);

        mockMvc.perform(get("/study/" + study.getEncodedPath() + "/settings/description"))
                .andExpect(status().isOk())
                .andExpect(view().name("study/settings/description"));
    }

    @Test
    @WithUserDetails(value = "tony",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("스터디 소개 수정 - 실패")
    void updateDescription_fail() throws Exception {
        Account tony = accountRepository.findByNickname("tony");
        Study study = createStudy("test-study", tony);

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
        Study study = createStudy("test-study", tony);

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