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

    public List<WorkDay> getWorkDaysByKW(Integer KW) {
        List<WorkDay> workDays = workDayRepository.findByKW(KW);
        return workDays;
        //return workDayRepository.findByKW(KW);
    }

    public List<WorkDay> getAllWorkDays() {
        return workDayRepository.findAll();
    }


    public List<WorkDay> getWorkDaysBydayOfWeek(String dayOfWeek) {
        return workDayRepository.findByDayOfWeek(dayOfWeek);
    }

    public List<WorkDay> getWorkDaysByPersonalId(String personalId) {
        return workDayRepository.findByPersonalId(personalId);
    }

    public List<WorkDay> getWorkDaysByPersonalIdAndDayOfWeek(String personalId, String dayOfWeek) {
        return workDayRepository.findByPersonalIdAndDayOfWeek(personalId,dayOfWeek);
    }

    public List<WorkDay> getWorkDayByPersonalIdAndKW(String personalId, Integer KW) {
        return workDayRepository.findByPersonalIdAndKW(personalId,KW);
    }

    public List<WorkDay> getWorkDaysByCustomParameter(String personalId, String dayOfWeek, Integer KW, String sortField, String sortDir) {
        Specification<WorkDay> specification = Specification.where(WorkDaySpecifications.hasPersonalId(personalId))
                .and(WorkDaySpecifications.hasDayOfWeek(dayOfWeek))
                .and(WorkDaySpecifications.hasKW(KW));
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        return workDayRepository.findAll(specification, sort);
        //return workDayRepository.findAll(specification);
    }
    public Optional<WorkDay> findWorkDayByPersonalIdAndDayOfWeekAndKW(String personalId, String dayOfWeek, Integer KW) {
        return workDayRepository.findByPersonalIdAndDayOfWeekAndKW(personalId, dayOfWeek, KW);
    }

    public WorkDay addOrUpdateWorkDay(WorkDay workDay) {
        return workDayRepository.save(workDay);
    }

    public Optional<WorkDay> findWorkDayById(Long workDayId) {
        return workDayRepository.findById(workDayId);
    }

    @Transactional
    public WorkDay updateWorkDay(Long workDayId, WorkDay updatedWorkDay) {
        // Pobranie istniejącego obiektu Route z bazy danych
        WorkDay existingWorkDay = workDayRepository.findById(workDayId).orElse(null);
        if (existingWorkDay != null) {
            // Aktualizacja pól istniejącej trasy na podstawie zaktualizowanych wartości
            existingWorkDay.setStartOfWork(updatedWorkDay.getStartOfWork());
            existingWorkDay.setPause(updatedWorkDay.getPause());
            existingWorkDay.setEndOfWork(updatedWorkDay.getEndOfWork());
            existingWorkDay.setTotalDistance(updatedWorkDay.getTotalDistance());
            existingWorkDay.setAccident(updatedWorkDay.isAccident());
            existingWorkDay.setFaults(updatedWorkDay.isFaults());

            // Zapis zaktualizowanej trasy w bazie danych
            return workDayRepository.save(existingWorkDay);
        }
        return null;
    }

    public void deleteWorkDay(WorkDay workDay){
        workDayRepository.delete(workDay);
    }
}

