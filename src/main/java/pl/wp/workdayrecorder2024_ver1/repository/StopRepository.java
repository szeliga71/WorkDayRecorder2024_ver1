package pl.wp.workdayrecorder2024_ver1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wp.workdayrecorder2024_ver1.model.Stop;

import java.util.List;

@Repository
public interface StopRepository extends JpaRepository<Stop,Long> {

    List<Stop>findAllByRouteId(Long routeId);

    Stop findStopById(Long stopId);
}

