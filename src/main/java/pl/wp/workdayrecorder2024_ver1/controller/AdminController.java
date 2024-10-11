package pl.wp.workdayrecorder2024_ver1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.wp.workdayrecorder2024_ver1.model.Employee;
import pl.wp.workdayrecorder2024_ver1.model.EmployeeSearchObject;
import pl.wp.workdayrecorder2024_ver1.model.WorkDay;
import pl.wp.workdayrecorder2024_ver1.model.WorkDaySearchObject;
import pl.wp.workdayrecorder2024_ver1.service.EmployeeService;
import pl.wp.workdayrecorder2024_ver1.service.WorkDayService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    EmployeeService employeeService;
    @Autowired
    WorkDayService workDayService;


    private final BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

    @GetMapping("/adminPanel")
    public String adminPanel(@AuthenticationPrincipal Employee employee,Model model) {
        if (employee == null) {
            return "redirect:/login";
        }
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        return "admin/adminPanel";
    }

    @GetMapping("/employees")
    public String getEmployeesList(@AuthenticationPrincipal Employee employee,Model model) {
        if (employee == null) {
            return "redirect:/login";
        }
       model.addAttribute("employeeSearchObject" ,new EmployeeSearchObject());
        model.addAttribute("employees", employeeService.getAllEmployees());
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        return "admin/employees";
    }
    @PostMapping("/confirmDeletionEmployee")
    public String confirmDeletionEmployee(@AuthenticationPrincipal Employee loggedEmployee,@RequestParam("personalId") String personalId, Model model) {
        if (loggedEmployee == null) {
            return "redirect:/login";
        }
        model.addAttribute("fullName", loggedEmployee.getFirstName() + " " + loggedEmployee.getLastName());
        Employee employee = (Employee) employeeService.loadUserByUsername(personalId);
        if (employee != null) {
            model.addAttribute("employee", employee);
            return "admin/confirmDeletionEmployee";
        } else {
            model.addAttribute("error", "Employee with id: " + personalId + " not found.");
            return "redirect:/admin/employees";
        }
    }

    @PostMapping("/deleteEmployee")
    public String deleteEmployee(@AuthenticationPrincipal Employee loggedEmployee,@RequestParam("personalId") String personalId, Model model) {
        if (loggedEmployee == null) {
            return "redirect:/login";
        }
        model.addAttribute("fullName", loggedEmployee.getFirstName() + " " + loggedEmployee.getLastName());
        Employee employee = (Employee) employeeService.loadUserByUsername(personalId);
        if (employee != null) {
            employeeService.deleteEmployee(employee);
            model.addAttribute("message", "Employee with id: " + personalId + " has been removed.");
        } else {
            model.addAttribute("error", "Employee with id: " + personalId + " not found.");
        }
        return "admin/deleteEmployee";
    }

    @GetMapping("/confirmDeletionEmployee")
    public String confirmDeletionEmployee(@AuthenticationPrincipal Employee employee,Model model){
        if (employee == null) {
            return "redirect:/login";
        }
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        return "admin/confirmDeletionEmployee";

    }
    @GetMapping("/deleteEmployee")
    public String deleteEmployee(@AuthenticationPrincipal Employee employee,Model model){
        if (employee == null) {
            return "redirect:/login";
        }
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        return "admin/deleteEmployee";
    }
    @GetMapping("/updateEmployee")
    public String showUpdateEmployeeForm(@RequestParam("personalId") String personalId, Model model) {
        Employee employee = (Employee) employeeService.loadUserByUsername(personalId);
        if (employee != null) {
            model.addAttribute("employee", employee);
            return "admin/updateEmployee";
        } else {
            model.addAttribute("error", "Employee with id: " + personalId + " not found.");
            return "redirect:/admin/employees";
        }
    }


    @PostMapping("/saveEmployee")
    public String updateEmployee(@RequestParam("personalId") String personalId,
                                 @RequestParam("firstName") String firstName,
                                 @RequestParam("lastName") String lastName,
                                 @RequestParam("password") String password,
                                 @RequestParam("mobilNumber") String mobilNumber, Model model) {
        Employee employee = (Employee) employeeService.loadUserByUsername(personalId);
        if (employee != null) {
            employee.setFirstName(firstName);
            employee.setLastName(lastName);
            employee.setPassword(password);
            employee.setMobilNumber(mobilNumber);
            employeeService.saveEmployee(employee);
            model.addAttribute("message", "Employee updated successfully");
        } else {
            model.addAttribute("error", "Employee with id: " + personalId + " not found.");
        }
        return "redirect:/admin/employees";
    }
    @GetMapping("/searchWeek")
    public String searchByWeek(@AuthenticationPrincipal Employee employee,@RequestParam("KW") Integer KW, Model model) {
        if (employee == null) {
            return "redirect:/login";
        }
        model.addAttribute("workDaySearchObject", new WorkDaySearchObject());
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        return "admin/searchWeek";  // Widok formularza wyszukiwania
    }

    @PostMapping("/searchWeek")
    public String getSearchedDaysByWeek(@AuthenticationPrincipal Employee employee,
                                        @RequestParam("KW") Integer KW,
                                        Model model) {
        List<WorkDay> workDays = workDayService.getWorkDaysByKW(KW);
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        model.addAttribute("workDays", workDays);
        model.addAttribute("KW", KW);

        return "admin/searchWeek";  // Przekazanie wyników do widoku wyników
    }

   /* @GetMapping("/searchWeek")
    public String searchByWeekold(@AuthenticationPrincipal Employee employee,@RequestParam("KW") Integer KW, Model model){
        if (employee == null) {
            return "redirect:/login";
        }
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        model.addAttribute("workDaySearchObject" ,new WorkDaySearchObject());
        return "admin/searchWeek";
    }*/
    /*@GetMapping("/searchWeek")
    public String searchByWeek(@AuthenticationPrincipal Employee employee,
                                        @RequestParam(value="KW", required = false) Integer KW,
                                        Model model)
    {
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());

        if (KW != null) {
        List<WorkDay> workDays=workDayService.getWorkDaysByKW(KW);
        model.addAttribute("workDays", workDays);
        model.addAttribute("KW", KW);

        return "admin/resultsSearchedPage";
        }
        return "admin/searchWeek";
    }
    @GetMapping("/resultsSearchedPage")
    public String getSearchedDaysByWeek(@AuthenticationPrincipal Employee employee,@RequestParam("workDays") List<WorkDay>workDays ,Model model){
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        model.addAttribute("workDays",workDays);
        return "admin/resultsSearchedPage";
    }*/
   /*@GetMapping("/resultsSearchedPage")
    public String getSearchedDaysByWeek(@AuthenticationPrincipal Employee employee,
                                        @RequestParam("KW") Integer KW,
                                        @RequestParam("workDays") List<WorkDay>workDays ,
                                        Model model){
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        model.addAttribute("workDays", workDays);
        model.addAttribute("KW", KW);
        return "admin/resultsSearchedPage";
    }*/

}