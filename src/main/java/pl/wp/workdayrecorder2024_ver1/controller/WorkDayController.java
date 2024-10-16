package pl.wp.workdayrecorder2024_ver1.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.wp.workdayrecorder2024_ver1.model.Employee;
import pl.wp.workdayrecorder2024_ver1.model.WorkDay;
import pl.wp.workdayrecorder2024_ver1.model.WorkDaySearchObject;
import pl.wp.workdayrecorder2024_ver1.service.MarktService;
import pl.wp.workdayrecorder2024_ver1.service.TrailerService;
import pl.wp.workdayrecorder2024_ver1.service.TruckService;
import pl.wp.workdayrecorder2024_ver1.service.WorkDayService;
import java.time.temporal.WeekFields;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;


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
    public String workDay(@AuthenticationPrincipal Employee employee,Model model) {
        if (employee == null) {
            return "redirect:/login";
        }
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        model.addAttribute("personalId", employee.getPersonalId());
        return "workDay";
    }


    @PostMapping("/workDay")
    public String addNewWorkDay(@AuthenticationPrincipal Employee employee,
            //@RequestParam("personalId") String personalId,
                               // @RequestParam("date") LocalDateTime date,
                                @RequestParam("dayOfWeekName") String dayOfWeek,
                                //@RequestParam("dayOfWeek") Integer dayOfWeek,
                                @RequestParam("startOfWork") LocalDateTime startOfWork,
                                @RequestParam("pause") String pause,
                                @RequestParam("endOfWork") LocalDateTime endOfWork,
                                @RequestParam("totalDistance") String totalDistance,
                                @RequestParam(name="accident", required = false, defaultValue = "false") boolean accident,
                                @RequestParam(name="faults", required = false, defaultValue = "false") boolean faults,
                                @RequestParam("notes") String notes,

                            Model model){

        //wybranie dnia tygodnia i ustalenie daty dnia pracy

        DayOfWeek chosenDayOfWeek = DayOfWeek.valueOf(dayOfWeek.toUpperCase());
        LocalDate today = LocalDate.now();
        DayOfWeek todayDayOfWeek = today.getDayOfWeek();
        int daysDifference = chosenDayOfWeek.getValue() - todayDayOfWeek.getValue();
        LocalDate workDayDate=null;

        if(daysDifference < 2 && daysDifference >-2){
            workDayDate = today.plusDays(daysDifference);
        }else{
            model.addAttribute("error", "Nie można wybrać dnia: " + dayOfWeek);
            System.out.println(" wybrany dzien tygodnia jest zly, jest za pozno lub za wczesnie na wprowadzeanie daNYCH ");
            // przeslalbym do error z odp info uzyc message
            return "errorPage";
        }
        // Obliczanie numeru tygodnia
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int weekNumber = workDayDate.get(weekFields.weekOfWeekBasedYear());


        //sprawdzeie czy nie ma juz tego dnia dla tego uzytkownika  (nazwa dnia tygodnia, data ew kw) w bazie


        WorkDay workDay = new WorkDay();
        workDay.setPersonalId(employee.getPersonalId());
        workDay.setDate(workDayDate.atStartOfDay());
        workDay.setDayOfWeek(chosenDayOfWeek.name());
        workDay.setKW(weekNumber);
        workDay.setStartOfWork(startOfWork);
        workDay.setPause(pause);
        workDay.setEndOfWork(endOfWork);
        workDay.setTotalDistance(totalDistance);
        workDay.setAccident(accident);
        workDay.setFaults(faults);
        workDay.setNotes(notes);

        workDayService.addWorkDay(workDay);

        return "redirect:/newRoute?workDayId=" + workDay.getId();
    }
    @PostMapping("/workDay/update")
    public String updateWorkDay(WorkDay workDay) {
        return "workDay";
    }

    @GetMapping("/admin/searchWeek")
    public String searchByWeek(@AuthenticationPrincipal Employee employee,
                               Model model) {
        if (employee == null) {
            return "redirect:/login";
        }
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        return "admin/searchWeek";
    }

    @PostMapping("/admin/searchWeek")
    public String getSearchedDaysByWeek(@AuthenticationPrincipal Employee employee,
                                        @RequestParam("KW") Integer KW,
                                        Model model) {

        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        model.addAttribute("KW", KW);
        return "admin/resultsSearchedPage";
    }
    @GetMapping("/admin/resultsSearchedPage")
    public String showSearchedDaysByWeek(@AuthenticationPrincipal Employee employee,@RequestParam("KW") Integer KW,Model model){

        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        model.addAttribute("workDays", workDayService.getWorkDaysByKW(KW));
        model.addAttribute("KW", KW);

        return "admin/resultsSearchedPage";
    }

    @GetMapping("/admin/workDayList")
    public String allWorkDays(@AuthenticationPrincipal Employee employee,
                               Model model) {
        if (employee == null) {
            return "redirect:/login";
        }
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        return "admin/searchWeek";
    }

    @PostMapping("/admin/workDayList")
    public String getAllWorkDays(@AuthenticationPrincipal Employee employee,
                                        Model model) {

        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        model.addAttribute("allWorkDays", workDayService.getAllWorkDays());
        return "admin/resultsSearchedPage";
    }

}
