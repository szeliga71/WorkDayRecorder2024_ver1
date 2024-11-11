package pl.wp.workdayrecorder2024_ver1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.wp.workdayrecorder2024_ver1.model.Employee;
import pl.wp.workdayrecorder2024_ver1.model.Route;
import pl.wp.workdayrecorder2024_ver1.model.Stop;
import pl.wp.workdayrecorder2024_ver1.service.MarktService;
import pl.wp.workdayrecorder2024_ver1.service.RouteService;
import pl.wp.workdayrecorder2024_ver1.service.StopService;

import java.time.LocalDateTime;

@Controller
public class StopController {

    @Autowired
    private RouteService routeService;
    @Autowired
    private MarktService marktService;
    @Autowired
    private StopService stopService;

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

        // Pobierz WorkDay z bazy danych
        Route route = routeService.getRouteById(routeId);

        // Utwórz nowy obiekt Route i ustaw jego właściwości
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
}
