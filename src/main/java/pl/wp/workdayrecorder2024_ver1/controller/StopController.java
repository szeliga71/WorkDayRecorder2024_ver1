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
import pl.wp.workdayrecorder2024_ver1.model.Stop;
import pl.wp.workdayrecorder2024_ver1.model.WorkDay;
import pl.wp.workdayrecorder2024_ver1.service.MarktService;
import pl.wp.workdayrecorder2024_ver1.service.RouteService;
import pl.wp.workdayrecorder2024_ver1.service.StopService;
import pl.wp.workdayrecorder2024_ver1.service.WorkDayService;

import java.time.LocalDateTime;

@Controller
public class StopController {

    @Autowired
    private RouteService routeService;
    @Autowired
    private MarktService marktService;
    @Autowired
    private StopService stopService;
    @Autowired
    private WorkDayService workDayService;

    @GetMapping("/newStop")
    public String newStop(@AuthenticationPrincipal Employee employee,
                          @RequestParam("routeId") Long routeId,
                          @RequestParam(value = "workDayId",required = false) Long workDayId,
                          Model model) {
        if (employee == null) {
            return "redirect:/login";
        }
        WorkDay workDay = workDayService.getWorkDayById(workDayId);
        if (workDayId != null) {
            workDay = workDayService.getWorkDayById(workDayId);
            model.addAttribute("workDay", workDay);
        } else {
            model.addAttribute("workDay", null);
        }
        Route route = routeService.getRouteById(routeId);
        model.addAttribute("workDay", workDay);
        //model.addAttribute("stop", new Stop());
        model.addAttribute("route", route);
        model.addAttribute("workDayId", workDayId);
        model.addAttribute("markets", marktService.getAllMarkts());
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        return "newStop";
    }

    @PostMapping("/newStop")
    public String addStop(@RequestParam("routeId") Long routeId,
                           @RequestParam("marktId") String marktId,
                           @RequestParam("begin") LocalDateTime begin,
                           @RequestParam("endOfStop") LocalDateTime endOfStop,
                           @RequestParam("action") String action,
                           Model model) {

        // Pobierz route z bazy danych
        Route route = routeService.getRouteById(routeId);

        // Utwórz nowy obiekt stop i ustaw jego właściwości
        Stop stop = new Stop();
        stop.setRoute(route);
        stop.setMarktId(marktId);
        stop.setBeginn(begin);
        stop.setEndOfStopp(endOfStop);

        stopService.addStop(stop);

        route.getStops().add(stop);

        routeService.updateRoute(routeId,route);



        if ("addStop".equals(action)) {
            return "redirect:/newStop?routeId=" + route.getId() + "&workDayId=" + route.getWorkDay().getId();
        } else if ("addRoute".equals(action)) {
           // return "redirect:/newRoute?workDayId=" + route.getWorkDayId() ;
            return "redirect:/newRoute?workDayId=" + route.getWorkDay().getId() ;
        } else if ("summary".equals(action)) {
            return "redirect:/summary?workDayId=" + route.getWorkDay().getId() ;
            //return "redirect:/summary?workDay=" + route.getWorkDay() ;
        }
        //return "redirect:/summary?workDayId=" + route.getWorkDayId() ;
        return "redirect:/summary?workDayId=" + route.getWorkDay().getId() ;
    }





    @GetMapping("/editStop")
    public String editRoute(@AuthenticationPrincipal Employee employee,
                            @RequestParam("id") Long id,@RequestParam("workDayId") Long workDayId,
                            Model model) {
        if (employee == null) {
            return "redirect:/login";
        }

        if (id == null) {
            throw new IllegalArgumentException("Stop ID nie może być puste");
        }
        WorkDay workDay = workDayService.getWorkDayById(workDayId);
        Stop stop =stopService.getStopById(id);

        if(stop == null) {
            model.addAttribute("info", "Nie znaleziono stop.");
            return "error";
        }

        model.addAttribute("stop", stop);
        model.addAttribute("workDay", workDay);
        model.addAttribute("markets", marktService.getAllMarkts());
        model.addAttribute("selectedMarktNumber", stop.getMarktId());
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        return "editStop"; // Upewnij się, że nazwa widoku pasuje do Twojego pliku HTML
    }

    @PostMapping("/editStop")
    public String saveStop(
            @RequestParam("workDayId") Long workDayId,/*@RequestParam("routeId") Long routeId,*/@RequestParam("stopId") Long stopId,@ModelAttribute Stop stop) {

        // Zapisz dane w bazie danych
        stopService.updateStop(stopId,stop);

        // Przekieruj na stronę podsumowania
        return "redirect:/summary?workDayId=" + workDayId;
    }

    //===============================================
    //     USUWANIE
       @GetMapping("/confirmDeletionStop")
    public String confirmDeletionStop(@AuthenticationPrincipal Employee loggedEmployee, @RequestParam("id") Long id,@RequestParam(value = "workDayId", required = false) Long workDayId ,Model model) {
        if (loggedEmployee == null) {
            return "redirect:/login";
        }
        model.addAttribute("fullName", loggedEmployee.getFirstName() + " " + loggedEmployee.getLastName());


        Stop stop =stopService.getStopById(id);

        model.addAttribute("workdayId", workDayId);
        WorkDay workDay = workDayService.getWorkDayById(workDayId);
        model.addAttribute("workDay", workDay);
        if (stop == null) {
            model.addAttribute("error", "Stop with markt number: " + stop.getMarktId() + " not found.");
            return "redirect:/summary";
            //return "error";
        }
        else{
            model.addAttribute("stop", stop);
            return "confirmDeletionStop";
        }
    }

    @PostMapping("/deleteStop")
    public String eraseStop(@AuthenticationPrincipal Employee loggedEmployee,@RequestParam("id") Long id, Model model) {
        if (loggedEmployee == null) {
            return "redirect:/login";
        }
        model.addAttribute("fullName", loggedEmployee.getFirstName() + " " + loggedEmployee.getLastName());

        Stop stop =stopService.getStopById(id);
        Long workDayId = stop.getRoute().getWorkDay().getId();
        if (stop != null) {
            workDayId = stop.getRoute().getWorkDay().getId(); // Pobierz workDayId z relacji
            if (workDayId == null && stop != null) {
                workDayId = stop.getRoute().getWorkDay().getId();
            }
            stopService.deleteStop(id);

            model.addAttribute("workDayId", workDayId);
            model.addAttribute("stop", stop);
            model.addAttribute("message", "Stop with market number: " + stop.getMarktId() + " has been removed.");
        } else {
            model.addAttribute("error", /*"Stop with number: " + stop.getMarktId() + " not found.*/"Stop with the given ID not found.");
        }

        return "redirect:/summary?workDayId=" + workDayId;
        //return "deleteStop";
    }

    /*@GetMapping("/confirmDeletionStop")
    public String confirmDeletionStop(@AuthenticationPrincipal Employee employee,Model model){
        if (employee == null) {
            return "redirect:/login";
        }
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        return "confirmDeletionStop";

    }*/

    @GetMapping("/deleteStop")
    public String deleteStop(@AuthenticationPrincipal Employee employee,@RequestParam(value = "workDayId",required = false) Long workDayId,Model model){
        if (employee == null) {
            return "redirect:/login";
        }
        model.addAttribute("workDayId", workDayId);

        if (workDayId != null) {
            WorkDay workDay = workDayService.getWorkDayById(workDayId);
            model.addAttribute("workDay", workDay);
        } else {
            model.addAttribute("workDay", null);
        }
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        //return "redirect:/summary?workDayId=" + workDayId;

        return "deleteStop";
    }
}
