package pl.wp.workdayrecorder2024_ver1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.wp.workdayrecorder2024_ver1.model.Employee;
import pl.wp.workdayrecorder2024_ver1.repository.EmployeeRepository;

import java.util.List;

@Service
public class EmployeeService implements UserDetailsService {
    @Autowired
    EmployeeRepository employeeRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Employee findEmployeeByPersonalId(String personalId) {
        return employeeRepository.findByPersonalId(personalId);
    }

    public UserDetails loadUserByUsername(String personalId) throws UsernameNotFoundException {
        Employee employee = findEmployeeByPersonalId(personalId);
        if (employee == null) {
            return null;
        }
        return User.withUsername(employee.getPersonalId())
                .password(employee.getPassword())
                .roles(String.valueOf(employee.getRole()))
                .build();
    }

    public void saveEmployee(Employee employee) {
        try {
            String encodedPassword = passwordEncoder.encode(employee.getPassword());
            employee.setPassword(encodedPassword);
            employeeRepository.save(employee);
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error while saving employee: "+e.getMessage());
        }
    }
    public void deleteEmployee(Employee employee) {
        employeeRepository.delete(employee);
    }
    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }
}
