package pl.wp.workdayrecorder2024_ver1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wp.workdayrecorder2024_ver1.model.Markt;


@Repository
public interface MarktRepository extends JpaRepository<Markt,String> {

    Markt findByMarktId(String marktId);
}