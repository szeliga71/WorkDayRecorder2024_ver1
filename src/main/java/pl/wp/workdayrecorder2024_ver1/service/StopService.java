package pl.wp.workdayrecorder2024_ver1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.wp.workdayrecorder2024_ver1.model.Stop;
import pl.wp.workdayrecorder2024_ver1.repository.StopRepository;
import java.util.List;

@Service
public class StopService {
    @Autowired
    private StopRepository stopRepository;

    public void addStop(Stop stop) {
        stopRepository.save(stop);
    }

    public Stop getStopById(Long stopId) {
        return stopRepository.findStopById(stopId);
    }

    public List<Stop> getAllStopsByRouteId(Long routeId) {
        return stopRepository.findAllByRouteId(routeId);
    }

    @Transactional
    public Stop updateStop(Long stopId, Stop updatedStop) {
        Stop existingStop = stopRepository.findById(stopId).orElse(null);
        if (existingStop != null) {
            existingStop.setBeginn(updatedStop.getBeginn());
            existingStop.setEndOfStopp(updatedStop.getEndOfStopp());
            existingStop.setMarktId(updatedStop.getMarktId());

            return stopRepository.save(existingStop);
        }
        return null;
    }

    public void deleteStop(Long stopId) {
        stopRepository.deleteById(stopId);
    }
}