package com.StudyCafe_R.StudyCafe_R.event;

import com.StudyCafe_R.StudyCafe_R.account.CurrentAccount;
import com.StudyCafe_R.StudyCafe_R.domain.Account;
import com.StudyCafe_R.StudyCafe_R.domain.Study;
import com.StudyCafe_R.StudyCafe_R.event.form.EventForm;
import com.StudyCafe_R.StudyCafe_R.study.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.nio.channels.AcceptPendingException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/study/{path}")
public class EventController {

    private final StudyService studyService;

    @GetMapping("/new-event")
    public String newEventForm(@CurrentAccount Account account, @PathVariable String path, Model model) {
        Study study = studyService.getStudyToUpdateStatus(account, path);
        model.addAttribute(study);
        model.addAttribute(account);
        model.addAttribute(new EventForm());
        return "event/form";
    }
}
