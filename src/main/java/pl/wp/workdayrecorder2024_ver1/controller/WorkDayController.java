package pl.wp.workdayrecorder2024_ver1.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.wp.workdayrecorder2024_ver1.model.Employee;
import pl.wp.workdayrecorder2024_ver1.model.WorkDay;
import pl.wp.workdayrecorder2024_ver1.service.MarktService;
import pl.wp.workdayrecorder2024_ver1.service.TrailerService;
import pl.wp.workdayrecorder2024_ver1.service.TruckService;
import pl.wp.workdayrecorder2024_ver1.service.WorkDayService;

import java.time.temporal.WeekFields;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;


@Controller
public class WorkDayController {

    @Autowired
    WorkDayService workDayService;
    @Autowired
    TruckService truckService;
    @Autowired
    TrailerService trailerService;
    @Autowired
    MarktService marktService;

    @GetMapping("/workDay")
    public String workDay(@AuthenticationPrincipal Employee employee, Model model) {
        if (employee == null) {
            return "redirect:/login";
        }
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        model.addAttribute("personalId", employee.getPersonalId());
        return "workDay";
    }
    @PostMapping("/workDay")
    public String addOrUpdateWorkDay(@AuthenticationPrincipal Employee employee,
                                     @RequestParam("dayOfWeekName") String dayOfWeek,
                                     @RequestParam("startOfWork") LocalDateTime startOfWork,
                                     @RequestParam("pause") String pause,
                                     @RequestParam("endOfWork") LocalDateTime endOfWork,
                                     @RequestParam(value = "totalDistance", required = false) Integer totalDistance,
                                     @RequestParam(name = "accident", required = false, defaultValue = "false") boolean accident,
                                     @RequestParam(name = "faults", required = false, defaultValue = "false") boolean faults,
                                     Model model) {
        if (employee == null) {
            return "redirect:/login";
        }
        if (totalDistance == null) {
            totalDistance = 0;
        }
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());

        //mechanizm wybrania dayOfWeek i ustalenie daty dnia pracy - date

        DayOfWeek chosenDayOfWeek = DayOfWeek.valueOf(dayOfWeek.toUpperCase());
        LocalDate today = LocalDate.now();
        DayOfWeek todayDayOfWeek = today.getDayOfWeek();
        int daysDifference = chosenDayOfWeek.getValue() - todayDayOfWeek.getValue();
        LocalDate workDayDate = null;

        if (daysDifference < 2 && daysDifference > -2) {
            workDayDate = today.plusDays(daysDifference);
        } else if (daysDifference == -6) {
            workDayDate = today.plusDays(1);
        } else if (daysDifference == 6) {
            workDayDate = today.minusDays(1);
        } else {
            model.addAttribute("error", "Nie można wybrać dnia: " + dayOfWeek);
            System.out.println(" wybrany dzien tygodnia jest zly, jest za pozno lub za wczesnie na wprowadzeanie daNYCH ");
            return "error";
        }
        // Obliczanie numeru tygodnia KW alias weekNumber
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int weekNumber = workDayDate.get(weekFields.weekOfWeekBasedYear());
        Optional<WorkDay> existingWorkDayOpt = workDayService.findWorkDayByPersonalIdAndDayOfWeekAndKW(
                employee.getPersonalId(), chosenDayOfWeek.name(), weekNumber);

        if (existingWorkDayOpt.isPresent()) {
            WorkDay existingWorkDay = existingWorkDayOpt.get();
            Long workDayId = existingWorkDay.getId();
            model.addAttribute("workDayId", workDayId);
            model.addAttribute("workDay", existingWorkDay);
            model.addAttribute("info", "Rekord dla tego dnia już istnieje, możesz go edytować.");
            return "redirect:/summary?workDayId=" + workDayId;
        } else {
            WorkDay workDay = new WorkDay();
            workDay.setPersonalId(employee.getPersonalId());
            workDay.setDate(workDayDate);
            workDay.setDayOfWeek(chosenDayOfWeek.name());
            workDay.setKW(weekNumber);
            workDay.setStartOfWork(startOfWork);
            workDay.setPause(pause);
            workDay.setEndOfWork(endOfWork);
            workDay.setTotalDistance(totalDistance);
            workDay.setAccident(accident);
            workDay.setFaults(faults);
            workDayService.addWorkDay(workDay);
            return "redirect:/newRoute?workDayId=" + workDay.getId();
        }
    }
    @GetMapping("/editWorkDay")
    public String editWorkDay(@AuthenticationPrincipal Employee employee,
                              @RequestParam("id") Long workDayId,
                              Model model) {
        if (employee == null) {
            return "redirect:/login";
        }
        if (workDayId == null) {
            throw new IllegalArgumentException("WorkDay ID nie może być puste");
        }
        WorkDay workDay = workDayService.getWorkDayById(workDayId);
        if (workDay == null) {
            model.addAttribute("info", "Nie znaleziono WorkDay.");
            return "error";
        }
        model.addAttribute("workDay", workDay);
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        return "editWorkDay";
    }
    @PostMapping("/editWorkDay")
    public String saveWorkDay(
            @RequestParam("workDayId") Long workDayId,
            @ModelAttribute WorkDay workDay) {
        workDayService.updateWorkDay(workDayId, workDay);
        return "redirect:/summary?workDayId=" + workDayId;
    }
    @GetMapping("/confirmDeletionWorkDay")
    public String confirmDeletionWorkDay(@AuthenticationPrincipal Employee loggedEmployee,
                                         @RequestParam("id") Long id,
                                         @RequestParam(value = "workDayId") Long workDayId,
                                         Model model) {
        if (loggedEmployee == null) {
            return "redirect:/login";
        }
        model.addAttribute("fullName", loggedEmployee.getFirstName() + " " + loggedEmployee.getLastName());
        model.addAttribute("workDayId", workDayId);
        WorkDay workDay = workDayService.getWorkDayById(workDayId);
        model.addAttribute("workDay", workDay);
        return "confirmDeletionWorkDay";
    }
    @PostMapping("/deleteWorkDay")
    public String deleteWorkDay(@AuthenticationPrincipal Employee loggedEmployee, @RequestParam("workDayId") Long workDayId, Model model) {
        if (loggedEmployee == null) {
            return "redirect:/login";
        }
        model.addAttribute("fullName", loggedEmployee.getFirstName() + " " + loggedEmployee.getLastName());
        WorkDay workDay = workDayService.getWorkDayById(workDayId);
        if (workDay != null) {
            workDayService.deleteWorkDay(workDay);
            model.addAttribute("workDayId", workDayId);
            model.addAttribute("message", "WorkDay : " + workDay.getDayOfWeek() + " has been removed.");
        } else {
            model.addAttribute("error","Work Day with the given ID not found.");
        }
        if (loggedEmployee.getRole().contains("ROLE_ADMIN")) {
            return "redirect:/admin/adminPanel";
        } else {
            return "redirect:/home";
        }
    }
    @GetMapping("/deleteWorkDay")
    public String deleteRoute(@AuthenticationPrincipal Employee employee,
                              @RequestParam(value = "workDayId") Long workDayId,
                              Model model) {
        if (employee == null) {
            return "redirect:/login";
        }
        if (workDayId != null) {
            WorkDay workDay = workDayService.getWorkDayById(workDayId);
            model.addAttribute("workDay", workDay);
        } else {
            model.addAttribute("workDay", null);
        }
        model.addAttribute("workDayId", workDayId);
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        return "deleteWorkDay";
    }
}







