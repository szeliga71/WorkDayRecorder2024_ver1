package pl.wp.workdayrecorder2024_ver1.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.wp.workdayrecorder2024_ver1.model.Employee;
import pl.wp.workdayrecorder2024_ver1.service.EmployeeService;
import pl.wp.workdayrecorder2024_ver1.service.WorkDayService;

@Controller
@RequestMapping("/admin")
public class EmployeeListController {

    @Autowired
    EmployeeService employeeService;
    @Autowired
    WorkDayService workDayService;

    @GetMapping("/employees")
    public String getEmployeesList(@AuthenticationPrincipal Employee employee,
                                   @RequestParam(defaultValue = "personalId") String sortField,
                                   @RequestParam(defaultValue = "asc") String sortDir,
                                   @RequestParam(defaultValue = "0") int page,
                                   Model model) {
        if (employee == null) {
            return "redirect:/login";
        }
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Page<Employee> employeesPage = employeeService.getAllEmployees(PageRequest.of(page, 10, sort));


        //model.addAttribute("employeeSearchObject" ,new EmployeeSearchObject());
        //model.addAttribute("employees", employeeService.getAllEmployees());
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        model.addAttribute("employees", employeesPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", employeesPage.getTotalPages());
        model.addAttribute("totalItems", employeesPage.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equalsIgnoreCase("asc") ? "desc" : "asc");
        return "admin/employees";
    }

    @PostMapping("/confirmDeletionEmployee")
    public String confirmDeletionEmployee(@AuthenticationPrincipal Employee loggedEmployee,
                                          @RequestParam("personalId") String personalId,
                                          Model model) {
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
    public String deleteEmployee(@AuthenticationPrincipal Employee loggedEmployee,
                                 @RequestParam("personalId") String personalId,
                                 Model model) {
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
    public String confirmDeletionEmployee(@AuthenticationPrincipal Employee employee,
                                          Model model) {
        if (employee == null) {
            return "redirect:/login";
        }
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        return "admin/confirmDeletionEmployee";
    }
    @GetMapping("/deleteEmployee")
    public String deleteEmployee(@AuthenticationPrincipal Employee employee,
                                 Model model) {
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
}
