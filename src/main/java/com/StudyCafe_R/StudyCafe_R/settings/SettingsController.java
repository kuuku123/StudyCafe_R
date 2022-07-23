package com.StudyCafe_R.StudyCafe_R.settings;

import com.StudyCafe_R.StudyCafe_R.account.CurrentUser;
import com.StudyCafe_R.StudyCafe_R.domain.Account;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class SettingsController {

    @GetMapping("/settings/profile")
    public String profileUpdateForm(@CurrentUser Account account , Model model) {

        model.addAttribute(account);
        model.addAttribute(new Profile(account));

        return "settings/profile";
    }
}