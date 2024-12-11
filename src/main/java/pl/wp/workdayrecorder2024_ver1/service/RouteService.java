package pl.wp.workdayrecorder2024_ver1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.wp.workdayrecorder2024_ver1.model.Route;
import pl.wp.workdayrecorder2024_ver1.model.WorkDay;
import pl.wp.workdayrecorder2024_ver1.repository.RouteRepository;

import java.util.List;

@Service
public class RouteService {
    @Autowired
    private RouteRepository routeRepository;

    public void deleteRoute(Route route) {
    routeRepository.delete(route);
    }

    public void addRoute(Route route) {
        routeRepository.save(route);
    }

    public Route getRouteById(Long routeId) {
        return routeRepository.findById(routeId).get();
    }
    public List<Route> getAllRoutesByWorkDayId(Long workDayId) {
       return  routeRepository.findAllByWorkDayId(workDayId);
    }

    @Transactional
    public Route updateRoute(Long routeId, Route updatedRoute) {
        // Pobranie istniejącego obiektu Route z bazy danych
        Route existingRoute = routeRepository.findById(routeId).orElse(null);
        if (existingRoute != null) {
            // Aktualizacja pól istniejącej trasy na podstawie zaktualizowanych wartości
            existingRoute.setTruckNumber(updatedRoute.getTruckNumber());
            existingRoute.setTrailerNumber(updatedRoute.getTrailerNumber());
            existingRoute.setRouteNumber(updatedRoute.getRouteNumber());
            existingRoute.setStartOfRoute(updatedRoute.getStartOfRoute());
            existingRoute.setDepartureFromTheBase(updatedRoute.getDepartureFromTheBase());
            existingRoute.setArrivalToTheBase(updatedRoute.getArrivalToTheBase());
            existingRoute.setEndOfRoute(updatedRoute.getEndOfRoute());

            // Zapis zaktualizowanej trasy w bazie danych
            return routeRepository.save(existingRoute);
        }
        return null;
    }
}
