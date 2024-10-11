package pl.wp.workdayrecorder2024_ver1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wp.workdayrecorder2024_ver1.model.Stop;
import pl.wp.workdayrecorder2024_ver1.model.WorkDay;
import pl.wp.workdayrecorder2024_ver1.repository.StopRepository;

import java.util.List;

@Service
public class StopService {
    @Autowired
    private StopRepository stopRepository;

    public void addStop(Stop stop) {
        stopRepository.save(stop);
    }

    public Stop getStopByRouteId(Long stopId) {
        return stopRepository.findStopByRouteId(stopId);
    }

    public List<Stop> getAllStopsByRouteId(Long routeId) {
        return stopRepository.findAllByRouteId(routeId);
    }
}
