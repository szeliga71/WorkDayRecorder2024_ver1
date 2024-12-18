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
import pl.wp.workdayrecorder2024_ver1.model.Route;
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
    public String workDay(@AuthenticationPrincipal Employee employee,Model model) {
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
                                @RequestParam("totalDistance") String totalDistance,
                                @RequestParam(name="accident", required = false, defaultValue = "false") boolean accident,
                                @RequestParam(name="faults", required = false, defaultValue = "false") boolean faults,
                                @RequestParam("notes") String notes,
                            Model model) {

        if (employee == null) {
            return "redirect:/login";
        }
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());

        //mechanizm wybrania dayOfWeek i ustalenie daty dnia pracy - date

        DayOfWeek chosenDayOfWeek = DayOfWeek.valueOf(dayOfWeek.toUpperCase());
        LocalDate today = LocalDate.now();
        DayOfWeek todayDayOfWeek = today.getDayOfWeek();
        int daysDifference = chosenDayOfWeek.getValue() - todayDayOfWeek.getValue();
        System.out.println(daysDifference);
        LocalDate workDayDate = null;

        if (daysDifference < 2 && daysDifference > -2){
            workDayDate = today.plusDays(daysDifference);
            System.out.println("opcja1 ");
        }
           else if(daysDifference==-6){
                workDayDate = today.plusDays(1);
                System.out.println("opcja2 ");
            }
        else if(daysDifference==6){
            workDayDate = today.minusDays(1);
            System.out.println("opcja3 ");
        } else {
            model.addAttribute("error", "Nie można wybrać dnia: " + dayOfWeek);
            System.out.println(" wybrany dzien tygodnia jest zly, jest za pozno lub za wczesnie na wprowadzeanie daNYCH ");
            // przeslalbym do error z odp info uzyc message
            return "error";
        }
        // Obliczanie numeru tygodnia KW alias weekNumber
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int weekNumber = workDayDate.get(weekFields.weekOfWeekBasedYear());


        //sprawdzeie czy nie ma juz tego dnia dla tego uzytkownika  (nazwa dnia tygodnia, data ew kw) w bazie

        Optional<WorkDay> existingWorkDayOpt = workDayService.findWorkDayByPersonalIdAndDayOfWeekAndKW(
                employee.getPersonalId(), chosenDayOfWeek.name(), weekNumber);

        if (existingWorkDayOpt.isPresent()) {
            // Rekord już istnieje, wypełnienie formularza danymi i umożliwienie edycji
            WorkDay existingWorkDay = existingWorkDayOpt.get();
            Long workDayId = existingWorkDay.getId();
            model.addAttribute("workDayId", workDayId);
            model.addAttribute("workDay", existingWorkDay);
            model.addAttribute("info", "Rekord dla tego dnia już istnieje, możesz go edytować.");
            return "redirect:/summary?workDayId=" + workDayId;
            //return "summary";
           // return "editWorkDay";  // Zwracasz widok do edycji formularza

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
            workDay.setNotes(notes);

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
            return "editWorkDay"; // Upewnij się, że nazwa widoku pasuje do Twojego pliku HTML
        }
    @PostMapping("/editWorkDay")
    public String saveWorkDay(
            @RequestParam("workDayId") Long workDayId,@ModelAttribute WorkDay workDay) {

        // Zapisz dane w bazie danych
        workDayService.updateWorkDay(workDayId, workDay);

        // Przekieruj na stronę podsumowania
        return "redirect:/summary?workDayId=" + workDayId;
    }

    //===============================================
    //     USUWANIE
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

       /* if (workDay == null) {
            model.addAttribute("error", "WorkDay with: " + workDay.getDayOfWeek() + " " + "not found.");
            return "redirect:/summary";
        }
        else{
            model.addAttribute("workDay" ,workDay);
            return "confirmDeletionWorkDay";
        }*/
        return "confirmDeletionWorkDay";
    }

    @PostMapping("/deleteWorkDay")
    public String deleteWorkDay(@AuthenticationPrincipal Employee loggedEmployee,@RequestParam("workDayId") Long workDayId, Model model) {
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
            model.addAttribute("error", /*"Stop with number: " + stop.getMarktId() + " not found.*/"Work Day with the given ID not found.");
        }
        if (loggedEmployee.getRole().contains("ROLE_ADMIN")) {
            return "redirect:/admin/adminPanel";
        } else {
            return "redirect:/home";
        }
        //return "redirect:/summary?workDayId=" + workDayId;

    }

    /*@GetMapping("/confirmDeletionStop")
    public String confirmDeletionStop(@AuthenticationPrincipal Employee employee,Model model){
        if (employee == null) {
            return "redirect:/login";
        }
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        return "confirmDeletionStop";

    }*/

    @GetMapping("/deleteWorkDay")
    public String deleteRoute(@AuthenticationPrincipal Employee employee,@RequestParam(value = "workDayId") Long workDayId,Model model){
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







