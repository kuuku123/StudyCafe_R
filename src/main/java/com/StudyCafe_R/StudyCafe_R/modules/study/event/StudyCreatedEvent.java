package com.StudyCafe_R.StudyCafe_R.modules.study.event;

import com.StudyCafe_R.StudyCafe_R.modules.study.domain.Study;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEvent;

@Getter
@RequiredArgsConstructor
public class StudyCreatedEvent {

    private Study study;

    public StudyCreatedEvent(Study study) {
        this.study = study;
    }

}
