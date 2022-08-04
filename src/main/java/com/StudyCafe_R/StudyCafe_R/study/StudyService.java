package com.StudyCafe_R.StudyCafe_R.study;

import com.StudyCafe_R.StudyCafe_R.domain.*;
import com.StudyCafe_R.StudyCafe_R.study.form.StudyDescriptionForm;
import lombok.RequiredArgsConstructor;
import org.modelmapper.AbstractCondition;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyService {

    private final StudyRepository studyRepository;
    private final ModelMapper modelMapper;

    public Study createNewStudy(Study study, Account account) {
        Study newStudy = studyRepository.save(study);

        AccountStudyManager accountStudyManager = AccountStudyManager.builder()
                .account(account)
                .study(study)
                .build();

        newStudy.addManager(accountStudyManager);
        return newStudy;
    }

    public Study getStudyToUpdate(Account account, String path) {
        Study study = getStudy(path);
        checkIfManager(account,study);
        return study;
    }


    public Study getStudy(String path) {
        Study study = studyRepository.findByPath(path);
        checkIfStudyExist(path,study);
        return study;
    }


    public void updateStudyDescription(Study study, StudyDescriptionForm studyDescriptionForm) {
        modelMapper.map(studyDescriptionForm,study);
    }

    public void updateStudyImage(Study study, String image) {
        study.setImage(image);
    }

    public void enableStudyBanner(Study study) {
        study.setUseBanner(true);
    }

    public void disableStudyBanner(Study study) {
        study.setUseBanner(false);
    }

    public void addTag(Study study, Tag tag) {
        StudyTag studyTag = StudyTag.builder()
                .study(study)
                .tag(tag).build();
        Study repoStudy = studyRepository.findByPath(study.getPath());
        boolean exist = repoStudy.getTags().stream()
                .anyMatch(st -> st.getTag() == tag);
        if(!exist) {
            repoStudy.addStudyTag(studyTag);
        }
    }

    public void removeTag(Study study, Tag tag) {
        Study repoStudy = studyRepository.findByPath(study.getPath());
        repoStudy.removeStudyTag(tag);
    }

    public void addZone(Study study, Zone zone) {
        StudyZone studyZone = StudyZone.builder()
                .zone(zone)
                .study(study)
                .build();
        Study repoStudy = studyRepository.findByPath(study.getPath());
        boolean exist = repoStudy.getZones().stream()
                .anyMatch(sz -> sz.getZone() == zone);
        if(!exist) {
            repoStudy.addStudyZone(studyZone);
        }
    }

    public void removeZone(Study study,Zone zone) {
        Study repoStudy = studyRepository.findByPath(study.getPath());
        repoStudy.removeStudyZone(zone);
        studyRepository.save(study);
    }

    public Study getStudyToUpdateTag(Account account, String path) {
        Study study = studyRepository.findAccountWithTagsByPath(path);
        checkIfStudyExist(path,study);
        checkIfManager(account,study);
        return study;
    }

    public Study getStudyToUpdateZone(Account account, String path) {
        Study study = studyRepository.findAccountWithZonesByPath(path);
        checkIfStudyExist(path,study);
        checkIfManager(account,study);
        return study;
    }

    private void checkIfManager(Account account, Study study) {
        if (!account.isManagerOf(study)){
            throw new AccessDeniedException("해당 기능을 사용할 수 없습니다.");
        }
    }

    private void checkIfStudyExist(String path, Study study) {
        if (study == null) {
            throw new IllegalArgumentException(path + "에 해당하는 스터디가 없습니다.");
        }

    }
}
