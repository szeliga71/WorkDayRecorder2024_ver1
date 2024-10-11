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
import pl.wp.workdayrecorder2024_ver1.model.WorkDay;
import pl.wp.workdayrecorder2024_ver1.service.RouteService;
import pl.wp.workdayrecorder2024_ver1.service.TrailerService;
import pl.wp.workdayrecorder2024_ver1.service.TruckService;
import pl.wp.workdayrecorder2024_ver1.service.WorkDayService;

import java.time.LocalDateTime;
@Controller
public class RouteController {

    @Autowired
    private TrailerService trailerService;
    @Autowired
    private TruckService truckService;
    @Autowired
    private RouteService routeService;
    @Autowired
    private WorkDayService workDayService;

    @GetMapping("/newRoute")
    public String newRoute(@AuthenticationPrincipal Employee employee,@RequestParam("workDayId") Long workDayId, Model model) {
        if (employee == null) {
            return "redirect:/login";
        }
        WorkDay workDay = workDayService.getWorkDayById(workDayId);
        // Dodaj obiekt Route do modelu
        //model.addAttribute("route", new Route());
        model.addAttribute("workDay", workDay);
        model.addAttribute("trucks", truckService.getAllTrucks());
        model.addAttribute("trailers", trailerService.getAllTrailers());
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        return "newRoute";
    }


    @PostMapping("/newRoute")
    public String addRoute(@RequestParam("workDayId") Long workDayId,
                           @RequestParam("truckNumber") String truckNumber,
                           @RequestParam("trailerNumber") String trailerNumber,
                           @RequestParam("routeNumber") String routeNumber,
                           @RequestParam("startOfRoute") LocalDateTime startOfRoute,
                           @RequestParam("departureFromTheBase") LocalDateTime departureFromTheBase,
                           @RequestParam("arrivalToTheBase") LocalDateTime arrivalToTheBase,
                           @RequestParam("endOfRoute") LocalDateTime endOfRoute,
                           @RequestParam("action") String action,
                           Model model) {

        // Pobierz WorkDay z bazy danych
        WorkDay workDay = workDayService.getWorkDayById(workDayId);

        // Utwórz nowy obiekt Route i ustaw jego właściwości
        Route route = new Route();
        route.setWorkDayId(workDayId);
        route.setTruckNumber(truckNumber);
        route.setTrailerNumber(trailerNumber);
        route.setRouteNumber(routeNumber);
        route.setStartOfRoute(startOfRoute);
        route.setDepartureFromTheBase(departureFromTheBase);
        route.setArrivalToTheBase(arrivalToTheBase);
        route.setEndOfRoute(endOfRoute);

        routeService.addRoute(route);
        // Dodaj Route do WorkDay
        workDay.getRoutes().add(route);

        // Zapisz zaktualizowany WorkDay z nowym Route
        workDayService.addWorkDay(workDay);

        // Wybierz następną akcję w zależności od wartości parametru 'action'
        if ("addStop".equals(action)) {
            System.out.println(route.getId());
            return "redirect:/newStop?routeId=" + route.getId();
        } else if ("addRoute".equals(action)) {
            return "redirect:/newRoute?workDayId=" + workDayId;
        } else if ("summary".equals(action)) {
            return "redirect:/summary?workDayId=" + workDayId;
        }


        // Domyślnie przekieruj do podsumowania, jeśli żadne warunki nie zostały spełnione
        return "redirect:/summary?workDayId=" + workDayId;
    }

}
