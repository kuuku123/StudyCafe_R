package com.StudyCafe_R.StudyCafe_R.event.validator;

import com.StudyCafe_R.StudyCafe_R.event.form.EventForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;

@Component
public class EventValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return EventForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EventForm eventForm = (EventForm) target;

        if(isNotValidEndEnrollmentDateTime(eventForm)){
            errors.rejectValue("endEnrollmentDateTime", "wrong.datetime","모입 접수 종료일시를 정확히 입력하세요");
        }

        if (isNotValidEndDateTime(eventForm)) {
            errors.rejectValue("endDateTime", "wrong.datetime","모입 접수 종료일시를 정확히 입력하세요");
        }

        if(isNotValidStartDateTime(eventForm)) {
            errors.rejectValue("startDateTime", "wrong.datetime","모입 접수 시작일시를 정확히 입력하세요");
        }

    }

    private boolean isNotValidStartDateTime(EventForm eventForm) {
        LocalDateTime startDateTime = eventForm.getStartDateTime();
        return startDateTime.isBefore(eventForm.getEndEnrollmentDateTime());
    }

    private boolean isNotValidEndEnrollmentDateTime(EventForm eventForm) {
        return eventForm.getEndEnrollmentDateTime().isBefore(LocalDateTime.now());
    }

    private boolean isNotValidEndDateTime(EventForm eventForm) {
        return eventForm.getEndDateTime().isBefore(eventForm.getStartDateTime()) || eventForm.getEndDateTime().isBefore(eventForm.getEndEnrollmentDateTime());
    }
}