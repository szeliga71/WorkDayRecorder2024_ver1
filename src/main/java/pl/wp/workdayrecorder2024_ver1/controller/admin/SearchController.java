package pl.wp.workdayrecorder2024_ver1.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.wp.workdayrecorder2024_ver1.model.Employee;
import pl.wp.workdayrecorder2024_ver1.model.WorkDay;
import pl.wp.workdayrecorder2024_ver1.service.MarktService;
import pl.wp.workdayrecorder2024_ver1.service.TrailerService;
import pl.wp.workdayrecorder2024_ver1.service.TruckService;
import pl.wp.workdayrecorder2024_ver1.service.WorkDayService;

import java.util.List;

@Controller
public class SearchController {

    @Autowired
    WorkDayService workDayService;
    @Autowired
    TruckService truckService;
    @Autowired
    TrailerService trailerService;
    @Autowired
    MarktService marktService;

    @GetMapping("/admin/resultsSearchedPage")
    public String showSearchedDaysByWeek(@AuthenticationPrincipal Employee employee,
                                         @RequestParam(value = "personalId", required = false) String personalId,
                                         @RequestParam(value = "dayOfWeek", required = false) String dayOfWeek,
                                         @RequestParam(value = "KW", required = false) Integer KW,
                                         @RequestParam(value = "sortField", defaultValue = "date") String sortField,
                                         @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir,
                                         Model model) {

        if (employee == null) {
            return "redirect:/login";
        }
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        List<WorkDay> workDays = workDayService.getWorkDaysByCustomParameter(personalId, dayOfWeek, KW, sortField, sortDir);
        model.addAttribute("workDays", workDays);
        model.addAttribute("personalId", personalId);
        model.addAttribute("dayOfWeek", dayOfWeek);
        model.addAttribute("KW", KW);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        return "admin/resultsSearchedPage";
    }

    @GetMapping("/admin/searchByCustomArguments")
    public String searchByAllArguments(@AuthenticationPrincipal Employee employee, Model model) {
        if (employee == null) {
            return "redirect:/login";
        }
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        return "admin/searchByCustomArguments";
    }

    @PostMapping("/admin/searchByCustomArguments")
    public String searchByAllArguments(@AuthenticationPrincipal Employee employee,
                                       @RequestParam(value = "personalId", required = false) String personalId,
                                       @RequestParam(value = "dayOfWeek", required = false) String dayOfWeek,
                                       @RequestParam(value = "KW", required = false) Integer KW,
                                       Model model) {
        if (employee == null) {
            return "redirect:/login";
        }
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        return "admin/resultsSearchedPage";
    }
}