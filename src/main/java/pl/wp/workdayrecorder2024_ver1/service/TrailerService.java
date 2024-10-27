package pl.wp.workdayrecorder2024_ver1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wp.workdayrecorder2024_ver1.model.Trailer;
import pl.wp.workdayrecorder2024_ver1.repository.TrailerRepository;

import java.util.List;

@Service
public class TrailerService {
    @Autowired
    TrailerRepository trailerRepository;

    public List<Trailer> getAllTrailers() {
        return trailerRepository.findAll();
    }

    public Trailer getTrailerByTrailerId(String trailerId) {
        return trailerRepository.findTrailerByTrailerId(trailerId);
    }

    public void deleteTrailer(String trailerId) {
        trailerRepository.deleteById(trailerId);
    }

    public void saveTrailer(Trailer trailer) {
        trailerRepository.save(trailer);
    }
}
