package pl.wp.workdayrecorder2024_ver1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wp.workdayrecorder2024_ver1.model.Markt;
import pl.wp.workdayrecorder2024_ver1.repository.MarktRepository;

import java.util.List;

@Service
public class MarktService {

    @Autowired
    private MarktRepository marktRepository;

    public List<Markt> getAllMarkets() {
        return marktRepository.findAll();
    }


}
