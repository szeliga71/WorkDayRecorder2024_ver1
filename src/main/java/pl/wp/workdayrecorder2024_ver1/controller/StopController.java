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
    public String newStop(@AuthenticationPrincipal Employee employee,@RequestParam("routeId") Long routeId, Model model) {
        if (employee == null) {
            return "redirect:/login";
        }
        Route route = routeService.getRouteById(routeId);
        //model.addAttribute("stop", new Stop());
        model.addAttribute("route", route);
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
        stop.setRouteId(routeId);
        stop.setMarktId(marktId);
        stop.setBeginn(begin);
        stop.setEndOfStopp(endOfStop);

        stopService.addStop(stop);

        route.getStops().add(stop);

        routeService.updateRoute(routeId,route);

        if ("addStop".equals(action)) {
            return "redirect:/newStop?routeId=" + route.getId();
        } else if ("addRoute".equals(action)) {
            return "redirect:/newRoute?workDayId=" + route.getWorkDayId() ;
        } else if ("summary".equals(action)) {
            return "redirect:/summary?workDayId=" + route.getWorkDayId() ;
        }
        return "redirect:/summary?workDayId=" + route.getWorkDayId() ;

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
    public String confirmDeletionStop(@AuthenticationPrincipal Employee loggedEmployee, @RequestParam("id") Long id, Model model) {
        if (loggedEmployee == null) {
            return "redirect:/login";
        }
        model.addAttribute("fullName", loggedEmployee.getFirstName() + " " + loggedEmployee.getLastName());

        Stop stop =stopService.getStopById(id);

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
    public String deleteStop(@AuthenticationPrincipal Employee loggedEmployee,@RequestParam("id") Long id, Model model) {
        if (loggedEmployee == null) {
            return "redirect:/login";
        }
        model.addAttribute("fullName", loggedEmployee.getFirstName() + " " + loggedEmployee.getLastName());
        Stop stop =stopService.getStopById(id);
        if (stop != null) {
            stopService.deleteStop(id);
            model.addAttribute("stop", stop);
            model.addAttribute("message", "Stop with market number: " + stop.getMarktId() + " has been removed.");
        } else {
            model.addAttribute("error", /*"Stop with number: " + stop.getMarktId() + " not found.*/"Stop with the given ID not found.");
        }
        return "deleteStop";
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
    public String deleteStop(@AuthenticationPrincipal Employee employee,Model model){
        if (employee == null) {
            return "redirect:/login";
        }
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        return "deleteStop";
    }
}
