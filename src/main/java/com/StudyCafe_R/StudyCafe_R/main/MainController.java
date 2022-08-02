package com.StudyCafe_R.StudyCafe_R.main;

import com.StudyCafe_R.StudyCafe_R.account.CurrentAccount;
import com.StudyCafe_R.StudyCafe_R.account.repository.AccountRepository;
import com.StudyCafe_R.StudyCafe_R.account.service.AccountService;
import com.StudyCafe_R.StudyCafe_R.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final AccountService accountService;
    private final AccountRepository accountRepository;

    @GetMapping("/")
    public String home(@CurrentAccount Account account, Model model) {
        if (account != null) {
            // login again to remove email validation message
            Account account1 = accountRepository.findById(account.getId()).get();

            accountService.login(account1);
            model.addAttribute(account1);
        }

        return "index";
    }

    @GetMapping("/login")
    public String login() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            return "redirect:/";
        }
        return "login";
    }
}
