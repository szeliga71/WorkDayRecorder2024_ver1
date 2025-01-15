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
import pl.wp.workdayrecorder2024_ver1.service.WorkDayExportToTagesberichtService;
import pl.wp.workdayrecorder2024_ver1.service.WorkDayService;
import java.util.ArrayList;
import java.util.List;

@Controller
public class WorkDayToTagesberichtController {

    @Autowired
    WorkDayService workDayService;

    @Autowired
    WorkDayExportToTagesberichtService workDayExportToTagesberichtService;

    @GetMapping("/admin/exportToTagesbericht")
    public String workDayToTagesbericht(@AuthenticationPrincipal Employee employee, @RequestParam("workDayIds") List<Long> workDayIds, Model model) {
        if (employee == null) {
            return "redirect:/login";
        }
        List<WorkDay> workDays = new ArrayList<>();
        for (Long workDayId : workDayIds) {
            workDays.add(workDayService.getWorkDayById(workDayId));
        }
        WorkDay workDay = workDays.get(0);
        Long workDayId = workDay.getId();
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        model.addAttribute("personalId", employee.getPersonalId());
        model.addAttribute("workDay", workDay);
        model.addAttribute("workDayId", workDayId);
        return "admin/exportToTagesbericht";
    }

    @PostMapping("/admin/exportToTagesbericht")
    public String exportWorkDayToTagesbericht(@AuthenticationPrincipal Employee employee, @RequestParam("workDayIds") List<Long> workDayIds, Model model) {
        if (employee == null) {
            return "redirect:/login";
        }
        List<WorkDay> workDays = new ArrayList<>();
        for (Long workDayId : workDayIds) {
            workDays.add(workDayService.getWorkDayById(workDayId));
        }
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        for (WorkDay workDay : workDays) {
            Long workDayId = workDay.getId();
            model.addAttribute("workDay", workDay);
            model.addAttribute("workDayId", workDayId);
            workDayExportToTagesberichtService.exportToTagesbericht(workDay);
        }
        return "redirect:/admin/downloadTagesberichtExcellFiles";
    }
}