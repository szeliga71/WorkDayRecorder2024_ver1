package pl.wp.workdayrecorder2024_ver1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wp.workdayrecorder2024_ver1.model.WorkDay;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkDayRepository extends JpaRepository<WorkDay, Long>, JpaSpecificationExecutor<WorkDay> {

    Optional<WorkDay> findByPersonalIdAndDayOfWeekAndKW(String personalId, String dayOfWeek, Integer KW);

}