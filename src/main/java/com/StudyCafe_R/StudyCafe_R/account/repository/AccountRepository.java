package com.StudyCafe_R.StudyCafe_R.account.repository;

import com.StudyCafe_R.StudyCafe_R.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account,Long> {

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);
}
