package pl.wp.workdayrecorder2024_ver1.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.wp.workdayrecorder2024_ver1.model.Route;
import pl.wp.workdayrecorder2024_ver1.model.Stop;
import pl.wp.workdayrecorder2024_ver1.model.Truck;
import pl.wp.workdayrecorder2024_ver1.model.WorkDay;
import pl.wp.workdayrecorder2024_ver1.service.MarktService;
import pl.wp.workdayrecorder2024_ver1.service.TrailerService;
import pl.wp.workdayrecorder2024_ver1.service.TruckService;
import pl.wp.workdayrecorder2024_ver1.service.WorkDayService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


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
    public String workDay(Model model) {
        model.addAttribute("trucks", truckService.getAllTrucks());
        model.addAttribute("trailers", trailerService.getAllTrailers());
        model.addAttribute("markets", marktService.getAllMarkets());
        return "workDay";
    }

    @PostMapping("/workDay")
    public String addNewWorkDay(@RequestParam("personalId") String personalId,
                                @RequestParam("date") LocalDateTime date,
                                @RequestParam("dayOfWeek") Integer dayOfWeek,
                                @RequestParam("startOfWork") LocalDateTime startOfWork,
                                @RequestParam("pause") String pause,
                                @RequestParam("endOfWork") LocalDateTime endOfWork,
                                @RequestParam("totalDistance") String totalDistance,
                                @RequestParam(name="accident", required = false, defaultValue = "false") boolean accident,
                                @RequestParam(name="faults", required = false, defaultValue = "false") boolean faults,
                                @RequestParam("notes") String notes,

                                @RequestParam("numberOfRoutes") Integer numberOfRoutes,

                                @RequestParam("truckNumber") String truckNumber,
                                @RequestParam("trailerNumber") String trailerNumber,
                                @RequestParam("idRoute") String idRoute,
                                @RequestParam("startOfRoute") LocalDateTime startOfRoute,
                                @RequestParam("departureFromTheBase") LocalDateTime departureFromTheBase,
                                @RequestParam("arrivalToTheBase") LocalDateTime arrivalToTheBase,
                                @RequestParam("endOfRoute") LocalDateTime endOfRoute,


                                @RequestParam("numberOfRoutes") Integer numberOfStops,

                                @RequestParam("marktId") String marktId,
                                @RequestParam("beginn") LocalDateTime beginn,
                                @RequestParam("endOfStopp") LocalDateTime endOfStopp,
                                Model model) {


        // Utwórz obiekt WorkDay
        WorkDay workDay = new WorkDay();
        workDay.setPersonalId(personalId);
        workDay.setDate(date);
        workDay.setDayOfWeek(dayOfWeek);
        workDay.setStartOfWork(startOfWork);
        workDay.setPause(pause);
        workDay.setEndOfWork(endOfWork);
        workDay.setTotalDistance(totalDistance);
        workDay.setAccident(accident);
        workDay.setFaults(faults);
        workDay.setNotes(notes);

        // Utwórz listę obiektów Route
        List<Route> routes = new ArrayList<>();
       // workDay.setRoutes(routes);


        for (int i = 0; i < numberOfRoutes; i++) {
            Route route = new Route();
            route.setTruckNumber(truckNumber);
            route.setTrailerNumber(trailerNumber);
            route.setIdRoute(idRoute);
            route.setStartOfRoute(startOfRoute);
            route.setDepartureFromTheBase(departureFromTheBase);
            route.setArrivalToTheBase(arrivalToTheBase);
            route.setEndOfRoute(endOfRoute);

            
            List<Stop> stops = new ArrayList<>();
            for (int j = 0; j < numberOfStops; j++) {
                Stop stop = new Stop();
                stop.setMarktId(marktId);
                stop.setBeginn(beginn);
                stop.setEndOfStopp(endOfStopp);

                stops.add(stop);
            }

            route.setStops(stops);
            routes.add(route);
        }
        // Ustaw listę routes dla WorkDay
        workDay.setRoutes(routes);

        // Zapisz obiekt WorkDay za pomocą serwisu
        workDayService.addWorkDay(workDay);

        // Przekieruj do strony sukcesu lub wyświetl komunikat
        return "redirect:/workDay";
    }


    @PostMapping("/workDay/update")
    public String updateWorkDay(WorkDay workDay) {
        return "workDay";
    }
}
