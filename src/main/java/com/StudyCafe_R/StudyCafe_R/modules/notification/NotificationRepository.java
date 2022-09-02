package com.StudyCafe_R.StudyCafe_R.modules.notification;

import com.StudyCafe_R.StudyCafe_R.modules.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
    long countByAccountAndChecked(Account account, boolean b);
}
