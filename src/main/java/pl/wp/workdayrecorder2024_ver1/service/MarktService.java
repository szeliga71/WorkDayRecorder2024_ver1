package pl.wp.workdayrecorder2024_ver1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wp.workdayrecorder2024_ver1.model.Markt;
import pl.wp.workdayrecorder2024_ver1.repository.MarktRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MarktService {

    @Autowired
    private MarktRepository marktRepository;

    public List<Markt> getAllMarkts() {
        return marktRepository.findAll();
    }


    public Markt getMarktByMarktId(String marktId) {
        return marktRepository.findByMarktId(marktId);
    }

    public void deleteMarkt(Markt markt) {
        marktRepository.delete(markt);
    }

    public void saveMarkt(Markt markt) {
        System.out.println(markt + "  zapisany ?");
        marktRepository.save(markt);
    }
}
