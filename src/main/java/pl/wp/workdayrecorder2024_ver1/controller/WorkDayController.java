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

  /*  @PostMapping("/workDay/edit")
    public String editWorkDay(@RequestParam("workDayId") Long workDayId,
                              @RequestParam("startOfWork") LocalDateTime startOfWork,
                              @RequestParam("pause") String pause,
                              @RequestParam("endOfWork") LocalDateTime endOfWork,
                              @RequestParam("totalDistance") String totalDistance,
                              @RequestParam("notes") String notes,
                              Model model) {

        Optional<WorkDay> workDayOpt = workDayService.findWorkDayById(workDayId);
        if (workDayOpt.isPresent()) {
            WorkDay workDay = workDayOpt.get();
            workDay.setStartOfWork(startOfWork);
            workDay.setPause(pause);
            workDay.setEndOfWork(endOfWork);
            workDay.setTotalDistance(totalDistance);
            workDay.setNotes(notes);

            workDayService.addOrUpdateWorkDay(workDay);
        }
        return "redirect:/home";
        //return "redirect:/workDays";  // Przekierowanie po zapisaniu
    }*/

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
            model.addAttribute("workDay", existingWorkDay);
            model.addAttribute("info", "Rekord dla tego dnia już istnieje, możesz go edytować.");
            return "summary";
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
}



  /*  @PostMapping("/workDay/update")
    public String updateWorkDay(WorkDay workDay) {
        return "workDay";
    }*/

    /*@GetMapping("/admin/resultsSearchedPage")
    public String showSearchedDaysByWeek(@AuthenticationPrincipal Employee employee,
                                         @RequestParam(value = "personalId", required = false) String personalId,
                                         @RequestParam(value = "dayOfWeek", required = false) String dayOfWeek,
                                         @RequestParam(value = "KW", required = false) Integer KW,
                                         Model model){

        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        List<WorkDay> workDays = workDayService.getWorkDaysByCustomParameter(personalId, dayOfWeek, KW);
        model.addAttribute("workDays", workDays);
        model.addAttribute("personalId", personalId);
        model.addAttribute("dayOfWeek", dayOfWeek);
        model.addAttribute("KW", KW);
        return "admin/resultsSearchedPage";
    }


    @GetMapping("/admin/searchByCustomArguments")
    public String searchByAllArguments(@AuthenticationPrincipal Employee employee,
                                       Model model){
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
                                       Model model){
        if (employee == null) {
            return "redirect:/login";
        }
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        return "admin/resultsSearchedPage";
    }*/


