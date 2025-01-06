package pl.wp.workdayrecorder2024_ver1.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class Route {

    @Id
    @GeneratedValue
    private Long id;
    private String truckNumber;
    private String trailerNumber;
    private String routeNumber;
    private Integer distance;
    private LocalDateTime startOfRoute;
    private LocalDateTime departureFromTheBase;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    //@JoinColumn(name = "route_id")
    private List<Stop> stops;
    private LocalDateTime arrivalToTheBase;
    private LocalDateTime endOfRoute;
    @ManyToOne
    //@JoinColumn(name = "work_day_id", nullable = false)
    private WorkDay workDay;
    //private Long workDayId;
    @Column(length = 1000)
    private String notes;


    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public WorkDay getWorkDay() {
        return workDay;
    }

    public void setWorkDay(WorkDay workDay) {
        this.workDay = workDay;
    }


   /* public Long getWorkDayId() {
        return workDayId;
    }

    public void setWorkDayId(Long workDayId) {
        this.workDayId = workDayId;
    }*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTruckNumber() {
        return truckNumber;
    }

    public void setTruckNumber(String truckNumber) {
        this.truckNumber = truckNumber;
    }

    public String getTrailerNumber() {
        return trailerNumber;
    }

    public void setTrailerNumber(String trailerNumber) {
        this.trailerNumber = trailerNumber;
    }

    public String getRouteNumber() {
        return routeNumber;
    }

    public void setRouteNumber(String routeNumber) {
        this.routeNumber = routeNumber;
    }

    public LocalDateTime getStartOfRoute() {
        return startOfRoute;
    }

    public void setStartOfRoute(LocalDateTime startOfRoute) {
        this.startOfRoute = startOfRoute;
    }

    public LocalDateTime getDepartureFromTheBase() {
        return departureFromTheBase;
    }

    public void setDepartureFromTheBase(LocalDateTime departureFromTheBase) {
        this.departureFromTheBase = departureFromTheBase;
    }

    public List<Stop> getStops() {
        return stops;
    }

    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }

    public LocalDateTime getArrivalToTheBase() {
        return arrivalToTheBase;
    }

    public void setArrivalToTheBase(LocalDateTime arrivalToTheBase) {
        this.arrivalToTheBase = arrivalToTheBase;
    }

    public LocalDateTime getEndOfRoute() {
        return endOfRoute;
    }

    public void setEndOfRoute(LocalDateTime endOfRoute) {
        this.endOfRoute = endOfRoute;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Route route)) return false;

        return Objects.equals(getId(), route.getId()) && Objects.equals(getTruckNumber(), route.getTruckNumber()) && Objects.equals(getTrailerNumber(), route.getTrailerNumber()) && Objects.equals(getRouteNumber(), route.getRouteNumber()) && Objects.equals(getStartOfRoute(), route.getStartOfRoute()) && Objects.equals(getDepartureFromTheBase(), route.getDepartureFromTheBase()) && Objects.equals(getStops(), route.getStops()) && Objects.equals(getArrivalToTheBase(), route.getArrivalToTheBase()) && Objects.equals(getEndOfRoute(), route.getEndOfRoute());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getId());
        result = 31 * result + Objects.hashCode(getTruckNumber());
        result = 31 * result + Objects.hashCode(getTrailerNumber());
        result = 31 * result + Objects.hashCode(getRouteNumber());
        result = 31 * result + Objects.hashCode(getStartOfRoute());
        result = 31 * result + Objects.hashCode(getDepartureFromTheBase());
        result = 31 * result + Objects.hashCode(getStops());
        result = 31 * result + Objects.hashCode(getArrivalToTheBase());
        result = 31 * result + Objects.hashCode(getEndOfRoute());
        return result;
    }
}