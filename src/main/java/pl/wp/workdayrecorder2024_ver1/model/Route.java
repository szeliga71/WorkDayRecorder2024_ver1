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
    private String idRoute;
    private LocalDateTime startOfRoute;
    private LocalDateTime departureFromTheBase;
    @OneToMany(cascade= CascadeType.ALL )
    private List<Stop> stops;
    private LocalDateTime arrivalToTheBase;
    private LocalDateTime endOfRoute;

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

    public String getIdRoute() {
        return idRoute;
    }

    public void setIdRoute(String idRoute) {
        this.idRoute = idRoute;
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

        return Objects.equals(getId(), route.getId()) && Objects.equals(getTruckNumber(), route.getTruckNumber()) && Objects.equals(getTrailerNumber(), route.getTrailerNumber()) && Objects.equals(getIdRoute(), route.getIdRoute()) && Objects.equals(getStartOfRoute(), route.getStartOfRoute()) && Objects.equals(getDepartureFromTheBase(), route.getDepartureFromTheBase()) && Objects.equals(getStops(), route.getStops()) && Objects.equals(getArrivalToTheBase(), route.getArrivalToTheBase()) && Objects.equals(getEndOfRoute(), route.getEndOfRoute());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getId());
        result = 31 * result + Objects.hashCode(getTruckNumber());
        result = 31 * result + Objects.hashCode(getTrailerNumber());
        result = 31 * result + Objects.hashCode(getIdRoute());
        result = 31 * result + Objects.hashCode(getStartOfRoute());
        result = 31 * result + Objects.hashCode(getDepartureFromTheBase());
        result = 31 * result + Objects.hashCode(getStops());
        result = 31 * result + Objects.hashCode(getArrivalToTheBase());
        result = 31 * result + Objects.hashCode(getEndOfRoute());
        return result;
    }
}
