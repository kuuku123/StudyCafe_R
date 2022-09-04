package com.StudyCafe_R.StudyCafe_R.modules.study;

import com.StudyCafe_R.StudyCafe_R.modules.study.domain.Study;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface StudyRepositoryExtension {

    List<Study> findByKeyword(String keyword);
}
