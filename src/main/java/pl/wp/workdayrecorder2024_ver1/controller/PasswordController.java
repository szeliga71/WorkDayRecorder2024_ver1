package pl.wp.workdayrecorder2024_ver1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.wp.workdayrecorder2024_ver1.model.Employee;
import pl.wp.workdayrecorder2024_ver1.model.PasswordForm;
import pl.wp.workdayrecorder2024_ver1.service.EmployeeService;

@Controller
public class PasswordController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/changePassword")
    public String showChangePasswordForm(@AuthenticationPrincipal Employee employee,Model model) {
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        model.addAttribute("passwordForm", new PasswordForm()); // Przekazanie obiektu formularza
        return "changePassword"; // Widok zmiany hasła
    }

    @PostMapping("/changePassword")
    public String changePassword(@AuthenticationPrincipal Employee employee, @ModelAttribute("passwordForm") PasswordForm form, Model model) {
        if (!form.getNewPassword().equals(form.getConfirmPassword())) {
            model.addAttribute("error", "Passwords do not match!");
            return "changePassword";
        }

        // Zmiana hasła w systemie
        employeeService.updatePassword(employee, form.getNewPassword());

        return "redirect:/login";
    }
}
