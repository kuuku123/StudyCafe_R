package com.StudyCafe_R.StudyCafe_R.event;

import com.StudyCafe_R.StudyCafe_R.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface EventRepository extends JpaRepository<Event,Long> {
}
