package pl.wp.dbasegameofthrone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.wp.dbasegameofthrone.model.UserApp;
import pl.wp.dbasegameofthrone.service.UserService;


@Controller
public class SecurityController {

    @Autowired
    UserService userService;

    @GetMapping("/login")
    public String getLoginPage() {
        return "security/login";
    }

    @GetMapping("/register")
    public String getRegistrationPage(Model model) {
        model.addAttribute("UserApp", new UserApp());
        return "security/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("UserApp") UserApp user, BindingResult result) {
        if (userService.loadUserByUsername(user.getEmail()) != null) { //user.getEmail()) != null) {
            result.rejectValue("confirmedPassword", "error.customUser", "Email is already in use");
            return "security/register";
        }
        if (user.getPassword() == null || user.getConfirmedPassword() == null ||
                !user.getPassword().equals(user.getConfirmedPassword())) {
            result.rejectValue("confirmedPassword", "error.customUser", "Passwords do not match");
            return "security/register";
        }
        if (result.hasErrors()) {
            return "security/register";
        }
        user.setRole("USER");
        System.out.println(user.getEmail());
        System.out.println(user.getRole());
        userService.saveUser(user);
        return "security/login";
    }
}
