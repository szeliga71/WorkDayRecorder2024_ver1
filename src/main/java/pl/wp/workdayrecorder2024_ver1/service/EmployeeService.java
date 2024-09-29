package pl.wp.workdayrecorder2024_ver1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.wp.workdayrecorder2024_ver1.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User findUserByUserId(String personalId) {
        return userRepository.findByPersonalId(personalId);
    }

    public UserDetails loadUserByUsername(String personalId) throws UsernameNotFoundException {
        User user = findUserByUserId(personalId);
        if (user == null) {
            return null;
        }
        return User.withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.)
                .build();
    }
}
