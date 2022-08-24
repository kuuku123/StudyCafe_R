package com.StudyCafe_R.StudyCafe_R.event;

import com.StudyCafe_R.StudyCafe_R.domain.Account;
import com.StudyCafe_R.StudyCafe_R.domain.Event;
import com.StudyCafe_R.StudyCafe_R.domain.Study;
import com.StudyCafe_R.StudyCafe_R.event.form.EventForm;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Transactional
@RequiredArgsConstructor
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;

    public Event createEvent(Event event, Study study, Account account) {
        event.setCreatedBy(account);
        event.setCreatedDateTime(LocalDateTime.now());
        event.setStudy(study);
        return eventRepository.save(event);
    }

    public void updateEvent(Event event, EventForm eventForm) {
        modelMapper.map(eventForm,event);
    }
}
