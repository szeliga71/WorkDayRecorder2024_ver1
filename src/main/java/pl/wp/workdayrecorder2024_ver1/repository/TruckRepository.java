package pl.wp.workdayrecorder2024_ver1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wp.workdayrecorder2024_ver1.model.Truck;

@Repository
public interface TruckRepository extends JpaRepository<Truck,String> {

    Truck findByNumber(String truckId);
}
