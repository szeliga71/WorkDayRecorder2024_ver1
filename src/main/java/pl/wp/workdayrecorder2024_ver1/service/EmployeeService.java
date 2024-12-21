package pl.wp.workdayrecorder2024_ver1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Override
    public UserDetails loadUserByUsername(String personalId) throws UsernameNotFoundException {
                return employeeRepository.findByPersonalId(personalId)
                .orElseThrow(() -> new UsernameNotFoundException("Employee not found: " + personalId));
    }
    public Employee findEmployeeByPersonalId(String personalId) throws UsernameNotFoundException {
        if(employeeRepository.findByPersonalId(personalId).isPresent()){
        return employeeRepository.findByPersonalId(personalId).get();
    }
        return null;}


    public void saveEmployee(Employee employee) {
        try {
            if (!employee.getPassword().startsWith("$2a$")) {
                String encodedPassword = passwordEncoder.encode(employee.getPassword());
                employee.setPassword(encodedPassword);
            }
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
    public Page<Employee> getAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    public void updatePassword(Employee employee, String newPassword) {
        employee.setPassword(passwordEncoder.encode(newPassword));
        employeeRepository.save(employee);
    }
}

/*
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.wp.workdayrecorder2024_ver1.model.Employee;
import pl.wp.workdayrecorder2024_ver1.repository.EmployeeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = new BCryptPasswordEncoder(); // Możesz przenieść to do konfiguracji, aby ponownie używać instancji enkodera
    }

    @Override
    public UserDetails loadUserByUsername(String personalId) throws UsernameNotFoundException {
        return employeeRepository.findByPersonalId(personalId)
                .orElseThrow(() -> new UsernameNotFoundException("Employee not found: " + personalId));
    }

    public Employee findEmployeeByPersonalId(String personalId) throws UsernameNotFoundException {
        return employeeRepository.findByPersonalId(personalId)
                .orElseThrow(() -> new UsernameNotFoundException("Employee not found: " + personalId));
    }

    public void saveEmployee(Employee employee) {
        try {
            // Sprawdź, czy hasło jest już zakodowane
            if (!isPasswordEncoded(employee.getPassword())) {
                employee.setPassword(passwordEncoder.encode(employee.getPassword()));
            }
            employeeRepository.save(employee);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error while saving employee: " + e.getMessage());
            throw new RuntimeException("Error saving employee", e); // Możesz rzucić konkretnym wyjątkiem biznesowym
        }
    }

    public void deleteEmployee(Employee employee) {
        try {
            employeeRepository.delete(employee);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error while deleting employee: " + e.getMessage());
            throw new RuntimeException("Error deleting employee", e);
        }
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Page<Employee> getAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    public void updatePassword(Employee employee, String newPassword) {
        if (newPassword == null || newPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        employee.setPassword(passwordEncoder.encode(newPassword));
        employeeRepository.save(employee);
    }

    private boolean isPasswordEncoded(String password) {
        return password != null && password.startsWith("$2a$");
    }
}*/
