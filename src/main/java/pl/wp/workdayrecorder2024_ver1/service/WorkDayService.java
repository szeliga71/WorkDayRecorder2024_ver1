package pl.wp.workdayrecorder2024_ver1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
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

    //public List<WorkDay> getWorkDaysByCustomParameter(String personalId, String dayOfWeek, Integer KW) {

        //return workDayRepository.findByPersonalIdAndDayOfWeekAndKW(personalId,dayOfWeek,KW);
    //}

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
    public List<WorkDay> getWorkDaysByCustomParameter(String personalId, String dayOfWeek, Integer KW) {
        Specification<WorkDay> specification = Specification.where(WorkDaySpecifications.hasPersonalId(personalId))
                .and(WorkDaySpecifications.hasDayOfWeek(dayOfWeek))
                .and(WorkDaySpecifications.hasKW(KW));

        return workDayRepository.findAll(specification);
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
}
