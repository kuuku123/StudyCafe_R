package com.StudyCafe_R.StudyCafe_R.modules.study.event;

import com.StudyCafe_R.StudyCafe_R.modules.study.domain.Study;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(readOnly = true)
@Component
@Async
public class StudyEventListener {

    @EventListener
    public void handleStudyCreatedEvent(StudyCreatedEvent studyCreatedEvent) {
        Study study = studyCreatedEvent.getStudy();
        log.info(study.getTitle() + " is created");
        // TODO 이메일 보내거나 , DB에 notification 저장하면 된다.
    }

}
