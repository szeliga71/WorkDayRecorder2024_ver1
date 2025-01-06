package pl.wp.workdayrecorder2024_ver1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wp.workdayrecorder2024_ver1.model.WorkDay;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkDayRepository extends JpaRepository<WorkDay, Long>, JpaSpecificationExecutor<WorkDay> {


    List<WorkDay> findByKW(Integer KW);

    Optional<WorkDay> findByPersonalIdAndDayOfWeekAndKW(String personalId, String dayOfWeek, Integer KW);

    List<WorkDay> findByDayOfWeek(String dayOfWeek);

    List<WorkDay> findByKWAndDayOfWeek(Integer KW, String dayOfWeek);

    List<WorkDay> findByPersonalId(String personalId);

    List<WorkDay> findByPersonalIdAndDayOfWeek(String personalId, String dayOfWeek);

    List<WorkDay> findByPersonalIdAndKW(String personalId, Integer KW);
}