package pl.wp.workdayrecorder2024_ver1.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class WorkDay {


    @Id
    @GeneratedValue
    private Long id;
    private String personalId;
    private LocalDate date;
    private String dayOfWeek;
    private int KW;
    private LocalDateTime startOfWork;
    private String pause;
    private LocalDateTime endOfWork;
    private String totalDistance;

    @OneToMany(/*mappedBy="workDay",*/cascade=CascadeType.ALL )
    private List<Route> routes;
    private boolean accident;
    private boolean faults;
    @Column(length = 1000)
    private String notes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getKW() {
        return KW;
    }

    public void setKW(int KW) {
        this.KW = KW;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalDateTime getStartOfWork() {
        return startOfWork;
    }

    public void setStartOfWork(LocalDateTime startOfWork) {
        this.startOfWork = startOfWork;
    }

    public String getPause() {
        return pause;
    }

    public void setPause(String pause) {
        this.pause = pause;
    }

    public LocalDateTime getEndOfWork() {
        return endOfWork;
    }

    public void setEndOfWork(LocalDateTime endOfWork) {
        this.endOfWork = endOfWork;
    }

    public String getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(String totalDistance) {
        this.totalDistance = totalDistance;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public boolean isAccident() {
        return accident;
    }

    public void setAccident(boolean accident) {
        this.accident = accident;
    }

    public boolean isFaults() {
        return faults;
    }

    public void setFaults(boolean faults) {
        this.faults = faults;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorkDay workDay)) return false;

        return isAccident() == workDay.isAccident() && isFaults() == workDay.isFaults() && Objects.equals(getId(), workDay.getId()) && Objects.equals(getDate(), workDay.getDate()) && Objects.equals(getDayOfWeek(), workDay.getDayOfWeek()) && Objects.equals(getStartOfWork(), workDay.getStartOfWork()) && Objects.equals(getPause(), workDay.getPause()) && Objects.equals(getEndOfWork(), workDay.getEndOfWork()) && Objects.equals(getTotalDistance(), workDay.getTotalDistance()) && Objects.equals(getRoutes(), workDay.getRoutes()) && Objects.equals(getNotes(), workDay.getNotes());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getId());
        result = 31 * result + Objects.hashCode(getDate());
        result = 31 * result + Objects.hashCode(getDayOfWeek());
        result = 31 * result + Objects.hashCode(getStartOfWork());
        result = 31 * result + Objects.hashCode(getPause());
        result = 31 * result + Objects.hashCode(getEndOfWork());
        result = 31 * result + Objects.hashCode(getTotalDistance());
        result = 31 * result + Objects.hashCode(getRoutes());
        result = 31 * result + Boolean.hashCode(isAccident());
        result = 31 * result + Boolean.hashCode(isFaults());
        result = 31 * result + Objects.hashCode(getNotes());
        return result;
    }
}
