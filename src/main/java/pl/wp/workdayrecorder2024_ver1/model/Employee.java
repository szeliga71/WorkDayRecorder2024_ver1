package pl.wp.workdayrecorder2024_ver1.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

import java.util.Objects;

@Entity
public class Employee {

    @Id
    private String personalId;
    private String firstName;
    private String lastName;
    private String password;
    private Role role;
    private String mobilNumber;
    @Transient
    private String confirmedPassword;

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getMobilNumber() {
        return mobilNumber;
    }

    public void setMobilNumber(String mobilNumber) {
        this.mobilNumber = mobilNumber;
    }

    public String getConfirmedPassword() {
        return confirmedPassword;
    }

    public void setConfirmedPassword(String confirmedPassword) {
        this.confirmedPassword = confirmedPassword;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee employee)) return false;

        return Objects.equals(getPersonalId(), employee.getPersonalId()) && Objects.equals(getFirstName(), employee.getFirstName()) && Objects.equals(getLastName(), employee.getLastName()) && Objects.equals(getPassword(), employee.getPassword()) && getRole() == employee.getRole() && Objects.equals(getMobilNumber(), employee.getMobilNumber()) && Objects.equals(getConfirmedPassword(), employee.getConfirmedPassword());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getPersonalId());
        result = 31 * result + Objects.hashCode(getFirstName());
        result = 31 * result + Objects.hashCode(getLastName());
        result = 31 * result + Objects.hashCode(getPassword());
        result = 31 * result + Objects.hashCode(getRole());
        result = 31 * result + Objects.hashCode(getMobilNumber());
        result = 31 * result + Objects.hashCode(getConfirmedPassword());
        return result;
    }
}
