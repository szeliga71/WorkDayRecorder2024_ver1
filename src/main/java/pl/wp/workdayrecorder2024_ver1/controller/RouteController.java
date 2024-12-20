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
    public String newRoute(@AuthenticationPrincipal Employee employee,
                           @RequestParam("workDayId") Long workDayId,
                           Model model) {
        if (employee == null) {
            return "redirect:/login";
        }
        WorkDay workDay = workDayService.getWorkDayById(workDayId);
        model.addAttribute("workDayId", workDayId);
        model.addAttribute("workDay", workDay);
        model.addAttribute("trucks", truckService.getAllTrucks());
        model.addAttribute("trailers", trailerService.getAllTrailers());
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        return "newRoute";
    }

    @PostMapping("/newRoute")
    public String addRoute(@RequestParam("workDayId") Long workDayId,
                           @RequestParam("truckNumber") String truckNumber,
                           @RequestParam(value = "trailerNumber", required = false) String trailerNumber,
                           @RequestParam("routeNumber") String routeNumber,
                           @RequestParam("distance") Integer distance,
                           @RequestParam("startOfRoute") LocalDateTime startOfRoute,
                           @RequestParam("departureFromTheBase") LocalDateTime departureFromTheBase,
                           @RequestParam("arrivalToTheBase") LocalDateTime arrivalToTheBase,
                           @RequestParam("endOfRoute") LocalDateTime endOfRoute,
                           @RequestParam("notes") String notes,
                           @RequestParam("action") String action,
                           Model model) {
        WorkDay workDay = workDayService.getWorkDayById(workDayId);
        Route route = new Route();
        route.setWorkDay(workDay);
        route.setTruckNumber(truckNumber);
        if (trailerNumber != null && !trailerNumber.isEmpty()) {
            route.setTrailerNumber(trailerNumber);
        } else {
            route.setTrailerNumber(null); // Brak przyczepy
        }
        route.setTrailerNumber(trailerNumber);
        route.setRouteNumber(routeNumber);
        route.setDistance(distance);
        route.setStartOfRoute(startOfRoute);
        route.setDepartureFromTheBase(departureFromTheBase);
        route.setArrivalToTheBase(arrivalToTheBase);
        route.setEndOfRoute(endOfRoute);
        route.setNotes(notes);
        routeService.addRoute(route);
        workDay.getRoutes().add(route);
        Integer dist = 0;
        for (Route rout : workDay.getRoutes()) {
            dist += rout.getDistance();
        }
        workDay.setTotalDistance(dist);
        workDayService.addWorkDay(workDay);
        // Wybierz następną akcję w zależności od wartości parametru 'action'
        if ("addStop".equals(action)) {
            return "redirect:/newStop?routeId=" + route.getId() + "&workDayId=" + route.getWorkDay().getId();
        } else if ("addRoute".equals(action)) {
            return "redirect:/newRoute?workDayId=" + workDayId;
        } else if ("summary".equals(action)) {
            return "redirect:/summary?workDayId=" + workDayId;
        }
        return "redirect:/summary?workDayId=" + workDayId;
    }

    @GetMapping("/editRoute")
    public String editRoute(@AuthenticationPrincipal Employee employee,
                            @RequestParam("id") Long id,
                            @RequestParam("workDayId") Long workDayId,
                            Model model) {
        if (employee == null) {
            return "redirect:/login";
        }
        if (id == null) {
            throw new IllegalArgumentException("Route ID nie może być puste");
        }
        Route route = routeService.getRouteById(id);
        if (route == null) {
            model.addAttribute("info", "Nie znaleziono route.");
            return "error";
        }
        WorkDay workDay = workDayService.getWorkDayById(workDayId);
        model.addAttribute("workDay", workDay);
        model.addAttribute("route", route);
        model.addAttribute("trucks", truckService.getAllTrucks());
        model.addAttribute("trailers", trailerService.getAllTrailers());
        model.addAttribute("selectedTruckNumber", route.getTruckNumber());
        model.addAttribute("selectedTrailerNumber", route.getTrailerNumber());
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        return "editRoute";
    }

    @PostMapping("/editRoute")
    public String saveRoute(@RequestParam("workDayId") Long workDayId,
                            @RequestParam("routeId") Long routeId,
                            @ModelAttribute Route route) {
        WorkDay workDay = workDayService.getWorkDayById(workDayId);
        routeService.updateRoute(routeId, route);
        Integer dist = 0;
        for (Route rout : workDay.getRoutes()) {
            dist += rout.getDistance();
        }
        workDay.setTotalDistance(dist);
        workDayService.updateWorkDay(workDayId, workDay);
        return "redirect:/summary?workDayId=" + workDayId;
    }

    @GetMapping("/confirmDeletionRoute")
    public String confirmDeletionStop(@AuthenticationPrincipal Employee loggedEmployee,
                                      @RequestParam("routeId") Long routeId,
                                      @RequestParam(value = "workDayId", required = false) Long workDayId,
                                      Model model) {
        if (loggedEmployee == null) {
            return "redirect:/login";
        }
        model.addAttribute("fullName", loggedEmployee.getFirstName() + " " + loggedEmployee.getLastName());

        Route route = routeService.getRouteById(routeId);
        model.addAttribute("workDayId", workDayId);
        WorkDay workDay = workDayService.getWorkDayById(workDayId);
        model.addAttribute("workDay", workDay);

        if (route == null) {
            model.addAttribute("error", "Route with number: " + route.getRouteNumber() + " not found.");
            return "redirect:/summary";
        } else {
            model.addAttribute("route", route);
            return "confirmDeletionRoute";
        }
    }

    @PostMapping("/deleteRoute")
    public String deleteStop(@AuthenticationPrincipal Employee loggedEmployee, @RequestParam("id") Long id, Model model) {
        if (loggedEmployee == null) {
            return "redirect:/login";
        }
        model.addAttribute("fullName", loggedEmployee.getFirstName() + " " + loggedEmployee.getLastName());
        Route route = routeService.getRouteById(id);
        Long workDayId = route.getWorkDay().getId();

        if (route != null) {
            workDayId = route.getWorkDay().getId(); // Pobierz workDayId z relacji
            if (workDayId == null && route != null) {
                workDayId = route.getWorkDay().getId();
            }
            routeService.deleteRoute(route);
            model.addAttribute("workDayId", workDayId);
            model.addAttribute("route", route);
            model.addAttribute("message", "Route with number: " + route.getRouteNumber() + " has been removed.");
        } else {
            model.addAttribute("error", "Route with the given ID not found.");
        }
        return "redirect:/summary?workDayId=" + workDayId;
    }

    @GetMapping("/deleteRoute")
    public String deleteRoute(@AuthenticationPrincipal Employee employee,
                              @RequestParam(value = "workDayId", required = false) Long workDayId,
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
        return "deleteRoute";
    }
}


