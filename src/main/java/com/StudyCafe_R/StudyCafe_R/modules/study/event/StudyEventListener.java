package com.StudyCafe_R.StudyCafe_R.modules.study.event;

import com.StudyCafe_R.StudyCafe_R.infra.config.AppProperties;
import com.StudyCafe_R.StudyCafe_R.infra.mail.EmailMessage;
import com.StudyCafe_R.StudyCafe_R.infra.mail.EmailService;
import com.StudyCafe_R.StudyCafe_R.modules.account.AccountPredicates;
import com.StudyCafe_R.StudyCafe_R.modules.account.AccountRepository;
import com.StudyCafe_R.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.StudyCafe_R.modules.notification.Notification;
import com.StudyCafe_R.StudyCafe_R.modules.notification.NotificationRepository;
import com.StudyCafe_R.StudyCafe_R.modules.notification.NotificationType;
import com.StudyCafe_R.StudyCafe_R.modules.study.StudyRepository;
import com.StudyCafe_R.StudyCafe_R.modules.study.domain.Study;
import com.StudyCafe_R.StudyCafe_R.modules.study.domain.StudyTag;
import com.StudyCafe_R.StudyCafe_R.modules.study.domain.StudyZone;
import com.StudyCafe_R.StudyCafe_R.modules.tag.Tag;
import com.StudyCafe_R.StudyCafe_R.modules.zone.Zone;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Async
@Transactional
@Component
@RequiredArgsConstructor
public class StudyEventListener {

    private final StudyRepository studyRepository;
    private final AccountRepository accountRepository;
    private final EmailService emailService;
    private final TemplateEngine templateEngine;
    private final AppProperties appProperties;
    private final NotificationRepository notificationRepository;
    @EventListener
    public void handleStudyCreatedEvent(StudyCreatedEvent studyCreatedEvent) {
        Study study = studyRepository.findStudyWithTagsAndZoneById(studyCreatedEvent.getStudy().getId());
        Set<Tag> tags = study.getTags().stream().map(StudyTag::getTag).collect(Collectors.toSet());
        Set<Zone> zones = study.getZones().stream().map(StudyZone::getZone).collect(Collectors.toSet());
        Iterable<Account> accounts = accountRepository.findAll(AccountPredicates.findByTagsAndZones(tags,zones));
        accounts.forEach(account -> {
            if(account.isStudyCreatedByEmail()) {
                sendStudyCreatedEmail(study, account);
            }

            if(account.isStudyCreatedByWeb()){
                saveStudyCreatedNotification(study, account);
            }
        });
    }

    private void saveStudyCreatedNotification(Study study, Account account) {
        Notification notification = new Notification();
        notification.setTitle(study.getTitle());
        notification.setLink("/study/" + study.getEncodedPath());
        notification.setChecked(false);
        notification.setCreatedLocalDateTime(LocalDateTime.now());
        notification.setMessage(study.getShortDescription());
        notification.setAccount(account);
        notification.setNotificationType(NotificationType.STUDY_CREATED);
        notificationRepository.save(notification);
    }

    private void sendStudyCreatedEmail(Study study, Account account) {
        Context context = new Context();
        context.setVariable("nickname", account.getNickname());
        context.setVariable("link","/study/" + study.getEncodedPath());
        context.setVariable("linkName", study.getTitle());
        context.setVariable("message","새로운 스터디가 생겼습니다.");
        context.setVariable("host",appProperties.getHost());
        String message = templateEngine.process("email/simple-link", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .subject("스터디카페, '" + study.getTitle() + "' 스터디가 생겼습니다.")
                .to(account.getEmail())
                .message(message)
                .build();

        emailService.sendEmail(emailMessage);
    }
}
