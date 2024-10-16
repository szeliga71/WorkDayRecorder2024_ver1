package pl.wp.workdayrecorder2024_ver1.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.wp.workdayrecorder2024_ver1.model.Employee;

@Controller
public class EmployeeController {


   @GetMapping("/home")
    public String home(Model model,@AuthenticationPrincipal Employee employee) {
        if (employee == null) {
            // Przekieruj lub zwróć odpowiedni komunikat, jeśli employee jest null
            return "errorPage"; }// lub inny widok do obsługi błędów
     model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        return "home";
    }


}
