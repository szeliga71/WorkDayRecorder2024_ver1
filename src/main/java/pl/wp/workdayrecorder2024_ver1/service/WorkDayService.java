package pl.wp.workdayrecorder2024_ver1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wp.workdayrecorder2024_ver1.model.WorkDay;
import pl.wp.workdayrecorder2024_ver1.repository.WorkDayRepository;

import java.util.List;

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
        System.out.println("Pobrane dni pracy: " + workDays);
        return workDays;
        //return workDayRepository.findByKW(KW);
    }

    public List<WorkDay> getAllWorkDays() {
        return workDayRepository.findAll();
    }

}
