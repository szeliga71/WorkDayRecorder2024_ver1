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

import static pl.wp.workdayrecorder2024_ver1.model.Role.ADMIN;
import static pl.wp.workdayrecorder2024_ver1.model.Role.USER;


@Controller
public class SecurityController {

    @Autowired
    EmployeeService employeeService;

    @GetMapping("/login")
    public String getLoginPage() {
        return "security/login";
    }

    @GetMapping("/register")
    public String getRegistrationPage(Model model) {
        model.addAttribute("Employee", new Employee());
        return "security/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("Employee") Employee employee, BindingResult result) {
        if (employeeService.loadUserByUsername(employee.getPersonalId()) != null) { //user.getEmail()) != null) {
            result.rejectValue("confirmedPassword", "error.employee", "Email is already in use");
            return "security/register";
        }
        if (employee.getPassword() == null || employee.getConfirmedPassword() == null ||
                !employee.getPassword().equals(employee.getConfirmedPassword())) {
            result.rejectValue("confirmedPassword", "error.employee", "Passwords do not match");
            return "security/register";
        }
        if (result.hasErrors()) {
            return "security/register";
        }
        employee.setRole(USER);

        employeeService.saveEmployee(employee);
        return "security/login";
    }
}
