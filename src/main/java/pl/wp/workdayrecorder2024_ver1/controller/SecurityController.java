package pl.wp.workdayrecorder2024_ver1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import pl.wp.workdayrecorder2024_ver1.service.EmployeeService;


@Controller
public class SecurityController {

    @Autowired
    EmployeeService employeeService;

    @GetMapping("/login")
    public String getLoginPage() {
        return "security/login";
    }

}
