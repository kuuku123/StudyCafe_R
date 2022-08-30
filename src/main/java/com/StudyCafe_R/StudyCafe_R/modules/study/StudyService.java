package com.StudyCafe_R.StudyCafe_R.modules.study;

import com.StudyCafe_R.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.StudyCafe_R.modules.account.domain.AccountStudyManager;
import com.StudyCafe_R.StudyCafe_R.modules.account.domain.AccountStudyMembers;
import com.StudyCafe_R.StudyCafe_R.modules.study.domain.Study;
import com.StudyCafe_R.StudyCafe_R.modules.study.domain.StudyTag;
import com.StudyCafe_R.StudyCafe_R.modules.study.domain.StudyZone;
import com.StudyCafe_R.StudyCafe_R.modules.study.form.StudyDescriptionForm;
import com.StudyCafe_R.StudyCafe_R.modules.study.form.StudyForm;
import com.StudyCafe_R.StudyCafe_R.modules.tag.Tag;
import com.StudyCafe_R.StudyCafe_R.modules.zone.Zone;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Study study = studyRepository.findStudyWithTagsByPath(path);
        checkIfStudyExist(path,study);
        checkIfManager(account,study);
        return study;
    }

    public Study getStudyToUpdateZone(Account account, String path) {
        Study study = studyRepository.findStudyWithZonesByPath(path);
        checkIfStudyExist(path,study);
        checkIfManager(account,study);
        return study;
    }

    public Study getStudyToUpdateStatus(Account account, String path) {
        Study study = studyRepository.findStudyWithManagersByPath(path);
        checkIfStudyExist(path, study);
        checkIfManager(account, study);
        return study;
    }

    private void checkIfManager(Account account, Study study) {
        if (!study.isManagedby(account)){
            throw new AccessDeniedException("해당 기능을 사용할 수 없습니다.");
        }
    }

    private void checkIfStudyExist(String path, Study study) {
        if (study == null) {
            throw new IllegalArgumentException(path + "에 해당하는 스터디가 없습니다.");
        }
    }

    public void publish(Study study) {
        study.publish();
    }

    public void close(Study study) {
        study.close();
    }

    public void startRecruit(Study study) {
        study.startRecruit();
    }

    public void stopRecruit(Study study) {
        study.stopRecruit();
    }

    public boolean isValidPath(String newPath) {
        if (!newPath.matches(StudyForm.VALID_PATH_PATTERN)) {
            return false;
        }

        return !studyRepository.existsByPath(newPath);
    }

    public void updateStudyPath(Study study, String newPath) {
        study.setPath(newPath);
    }

    public boolean isValidTitle(String newTitle) {
        return newTitle.length() <= 50;
    }

    public void updateStudyTitle(Study study, String newTitle) {
        study.setTitle(newTitle);
    }

    public void remove(Study study) {
        if (study.isRemovable()) {
            studyRepository.delete(study);
        } else {
            throw new IllegalArgumentException("스터디를 삭제할 수 없습니다.");
        }
    }

    public void addMember(Study study, Account account) {
        AccountStudyMembers member = AccountStudyMembers.builder()
                .account(account)
                .study(study)
                .build();
        study.addMember(member);
    }

    public void removeMember(Study study, Account account) {
        study.removeMember(account);
    }

    public Study getStudyToEnroll(String path) {
        Study study = studyRepository.findStudyOnlyByPath(path);
        checkIfStudyExist(path,study);
        return study;
    }
}
