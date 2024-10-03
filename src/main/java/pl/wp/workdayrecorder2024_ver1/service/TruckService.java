package pl.wp.workdayrecorder2024_ver1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wp.workdayrecorder2024_ver1.model.Truck;
import pl.wp.workdayrecorder2024_ver1.repository.TruckRepository;

import java.util.List;

@Service
public class TruckService {

    @Autowired
    private TruckRepository truckRepository;
    public List<Truck> getAllTrucks() {
        return truckRepository.findAll();
    }
}