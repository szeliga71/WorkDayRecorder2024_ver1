package pl.wp.workdayrecorder2024_ver1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.wp.workdayrecorder2024_ver1.model.Employee;
import pl.wp.workdayrecorder2024_ver1.service.EmployeeService;


@Controller
public class SecurityController {

    @Autowired
    EmployeeService employeeService;

    @GetMapping("/login")
    public String getLoginPage() {
        return "security/login";
    }

    /*@GetMapping("/admin/register")
    public String getRegistrationPage(Model model) {
        model.addAttribute("employee", new Employee());
        return "admin/register";
    }

    @PostMapping("/admin/register")
    public String registerUser(@ModelAttribute("employee") Employee employee, BindingResult result) {

        if (employeeService.findEmployeeByPersonalId(employee.getPersonalId())!=null) {
            result.rejectValue("confirmedPassword", "error.employee", "Email is already in use");
           return "admin/register";
        }
        if (employee.getPassword() == null || employee.getConfirmedPassword() == null ||
                !employee.getPassword().equals(employee.getConfirmedPassword())) {
            result.rejectValue("confirmedPassword", "error.employee", "Passwords do not match");
            return "admin/register";
        }
        if (result.hasErrors()) {
            return "admin/register";
        }
        employee.setRole("ROLE_USER");
        employeeService.saveEmployee(employee);
        return "admin/adminPanel";
    }*/
}
