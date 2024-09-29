package pl.wp.workdayrecorder2024_ver1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wp.workdayrecorder2024_ver1.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByPersonalId(String personalId);
}
