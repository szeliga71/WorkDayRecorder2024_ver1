package pl.wp.workdayrecorder2024_ver1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.wp.workdayrecorder2024_ver1.model.WorkDay;
import pl.wp.workdayrecorder2024_ver1.repository.WorkDayRepository;
import java.util.List;
import java.util.Optional;

@Service
public class WorkDayService {

    @Autowired
    WorkDayRepository workDayRepository;

    public void addWorkDay(WorkDay workDay) {
        workDayRepository.save(workDay);
    }

    public WorkDay getWorkDayById(Long workDayId) {
        return workDayRepository.findById(workDayId).get();
    }

    public List<WorkDay> getWorkDaysByCustomParameter(String personalId, String dayOfWeek, Integer KW, String sortField, String sortDir) {
        Specification<WorkDay> specification = Specification.where(WorkDaySpecifications.hasPersonalId(personalId))
                .and(WorkDaySpecifications.hasDayOfWeek(dayOfWeek))
                .and(WorkDaySpecifications.hasKW(KW));
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        return workDayRepository.findAll(specification, sort);
    }

    public Optional<WorkDay> findWorkDayByPersonalIdAndDayOfWeekAndKW(String personalId, String dayOfWeek, Integer KW) {
        return workDayRepository.findByPersonalIdAndDayOfWeekAndKW(personalId, dayOfWeek, KW);
    }

    @Transactional
    public WorkDay updateWorkDay(Long workDayId, WorkDay updatedWorkDay) {
        WorkDay existingWorkDay = workDayRepository.findById(workDayId).orElse(null);
        if (existingWorkDay != null) {
            existingWorkDay.setStartOfWork(updatedWorkDay.getStartOfWork());
            existingWorkDay.setPause(updatedWorkDay.getPause());
            existingWorkDay.setEndOfWork(updatedWorkDay.getEndOfWork());
            existingWorkDay.setTotalDistance(updatedWorkDay.getTotalDistance());
            existingWorkDay.setAccident(updatedWorkDay.isAccident());
            existingWorkDay.setFaults(updatedWorkDay.isFaults());
            return workDayRepository.save(existingWorkDay);
        }
        return null;
    }

    public void deleteWorkDay(WorkDay workDay) {
        workDayRepository.delete(workDay);
    }
}