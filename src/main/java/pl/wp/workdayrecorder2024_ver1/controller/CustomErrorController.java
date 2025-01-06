package pl.wp.workdayrecorder2024_ver1.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.wp.workdayrecorder2024_ver1.model.Employee;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(@AuthenticationPrincipal Employee employee, Model model) {
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        model.addAttribute("role", employee.getRole());
        // Zwróć widok dla strony błędu
        return "error";
    }
}