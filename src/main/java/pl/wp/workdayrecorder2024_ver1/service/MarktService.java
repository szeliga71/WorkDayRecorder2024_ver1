package pl.wp.workdayrecorder2024_ver1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.wp.workdayrecorder2024_ver1.model.Markt;
import pl.wp.workdayrecorder2024_ver1.repository.MarktRepository;
import java.util.List;

@Service
public class MarktService {

    @Autowired
    private MarktRepository marktRepository;

    public List<Markt> getAllMarkts() {
        return marktRepository.findAll();
    }

    public Page<Markt> getAllMarkts(Pageable pageable) {
        return marktRepository.findAll(pageable);
    }

    public Markt getMarktByMarktId(String marktId) {
        return marktRepository.findByMarktId(marktId);
    }

    public void deleteMarkt(Markt markt) {
        marktRepository.delete(markt);
    }

    public void saveMarkt(Markt markt) {
        marktRepository.save(markt);
    }
}