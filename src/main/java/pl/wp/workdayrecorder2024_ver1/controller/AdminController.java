package pl.wp.workdayrecorder2024_ver1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.wp.workdayrecorder2024_ver1.model.Employee;
import pl.wp.workdayrecorder2024_ver1.model.EmployeeSearchObject;
import pl.wp.workdayrecorder2024_ver1.model.Role;
import pl.wp.workdayrecorder2024_ver1.service.EmployeeService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    EmployeeService employeeService;


    @GetMapping("/adminPanel")
    public String adminPanel(Model model) {
        return "admin/adminPanel";
    }
    @GetMapping("/employees")
    public String getEmployeesList(Model model) {
        model.addAttribute("employeeSearchObject" ,new EmployeeSearchObject());
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "admin/employees";
    }

    @PostMapping("/employess")
    public String deleteEmployee(Model model, EmployeeSearchObject employeeSearchObject){
        String personalId = employeeSearchObject.getPersonalId();
        Employee employee = employeeService.findEmployeeByPersonalId(personalId);
        if (employee != null) {
            employeeService.deleteEmployee(employee);
            model.addAttribute("message", "Employee with id:  " + personalId + " has been removed.");
        } else {
            model.addAttribute("error", "Employee with id:  " + personalId + " not found.");
        }
        return "admin/deleteEmployee";
    }

    //==========================================
    @PostMapping("/changeRole")
    public String changeUserRole(@RequestParam("personalId") String personalId, @RequestParam("role") Role role, Model model) {
        Employee employee = employeeService.findEmployeeByPersonalId(personalId);
        if (employee != null) {
            employee.setRole(role);
            employeeService.saveEmployee(employee);  // Zapisanie zmian w bazie danych
            model.addAttribute("message", "Role of employee with id " + personalId + " was changed to " + role + ".");
        } else {
            model.addAttribute("error", "Employee with id: " + personalId + " not found!");
        }
        return "redirect:/admin/employees";
    }

    @PostMapping("/addEmployee")
    public String addEmployee(@RequestParam("firstName")String firstName,@RequestParam("lastName")String lastName,@RequestParam("mobilNumber")String mobilNumber,@RequestParam("personalId") String personalId, @RequestParam("password") String password,@RequestParam("password") String confirmedPassword,
                          @RequestParam("role") Role role, Model model) {
        Employee existingEmployee = employeeService.findEmployeeByPersonalId(personalId);

        if (existingEmployee == null) {
            Employee newEmployee = new Employee();
            newEmployee.setFirstName(firstName);
            newEmployee.setLastName(lastName);
            newEmployee.setMobilNumber(mobilNumber);
            newEmployee.setPersonalId(personalId);
            newEmployee.setPassword(password);
            newEmployee.setRole(role);

            employeeService.saveEmployee(newEmployee);

            model.addAttribute("message", "New Employee with id: " + personalId + " was added.");
        } else {
            model.addAttribute("error", "Employee with id: " + personalId + " exist.");
        }
        return "redirect:/admin/employees";
    }
}
