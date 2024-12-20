package pl.wp.workdayrecorder2024_ver1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.wp.workdayrecorder2024_ver1.model.Employee;
import pl.wp.workdayrecorder2024_ver1.model.Route;
import pl.wp.workdayrecorder2024_ver1.model.Stop;
import pl.wp.workdayrecorder2024_ver1.model.WorkDay;
import pl.wp.workdayrecorder2024_ver1.service.RouteService;
import pl.wp.workdayrecorder2024_ver1.service.StopService;
import pl.wp.workdayrecorder2024_ver1.service.WorkDayService;

import java.util.ArrayList;
import java.util.List;


@Controller
public class SummaryController {

    @Autowired
    WorkDayService workDayService;
    @Autowired
    RouteService routeService;
    @Autowired
    StopService stopService;

    @GetMapping("/summary")
    public String summary(@AuthenticationPrincipal Employee employee,
                          Model model,
                          @RequestParam("workDayId") Long workDayId) {
        if (employee == null) {
            return "redirect:/login";
        }
        if (workDayId == null) {
            throw new IllegalArgumentException("ID must not be null");
        }
        WorkDay workDay = workDayService.getWorkDayById(workDayId);
        List<Route> routes = routeService.getAllRoutesByWorkDayId(workDayId);
        for (Route route : routes) {
            Long idRoute = route.getId();
            List<Stop> stops = stopService.getAllStopsByRouteId(idRoute);
            route.setStops(stops);
        }
        workDay.setRoutes(routes);
        model.addAttribute("role", employee.getRole());
        model.addAttribute("workDay", workDay);
        model.addAttribute("routes", routes);
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        return "summary";
    }

}