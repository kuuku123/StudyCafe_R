package com.StudyCafe_R.StudyCafe_R.modules.main;

import com.StudyCafe_R.StudyCafe_R.modules.account.CurrentAccount;
import com.StudyCafe_R.StudyCafe_R.modules.account.AccountRepository;
import com.StudyCafe_R.StudyCafe_R.modules.account.service.AccountService;
import com.StudyCafe_R.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.StudyCafe_R.modules.study.StudyRepository;
import com.StudyCafe_R.StudyCafe_R.modules.study.domain.Study;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final StudyRepository studyRepository;

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

    @GetMapping("/search/study")
    public String searchStudy(String keyword, Model model) {
        List<Study> studyList = studyRepository.findByKeyword(keyword);
        model.addAttribute("studyList",studyList);
        model.addAttribute("keyword",keyword);
        return "search";
    }
}
