package pl.wp.workdayrecorder2024_ver1.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.wp.workdayrecorder2024_ver1.model.Employee;
import pl.wp.workdayrecorder2024_ver1.service.EmployeeService;
import pl.wp.workdayrecorder2024_ver1.service.WorkDayService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    EmployeeService employeeService;
    @Autowired
    WorkDayService workDayService;

    @GetMapping("/adminPanel")
    public String adminPanel(@AuthenticationPrincipal Employee employee, Model model) {
        if (employee == null) {
            return "redirect:/login";
        }
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        return "admin/adminPanel";
    }
}