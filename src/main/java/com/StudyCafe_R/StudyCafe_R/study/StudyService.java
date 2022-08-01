package com.StudyCafe_R.StudyCafe_R.study;

import com.StudyCafe_R.StudyCafe_R.domain.Account;
import com.StudyCafe_R.StudyCafe_R.domain.AccountStudy;
import com.StudyCafe_R.StudyCafe_R.domain.Study;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyService {

    private final StudyRepository studyRepository;
    private final EntityManager entityManager;

    public Study createNewStudy(Study study, Account account) {
        Study newStudy = studyRepository.save(study);

        AccountStudy accountStudy = AccountStudy.builder()
                .account(account)
                .study(study)
                .build();

        newStudy.addManager(accountStudy);
        return newStudy;
    }
}
