package pl.wp.workdayrecorder2024_ver1.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Stop {

    @Id
    @GeneratedValue
    private Long id;
    private LocalDateTime beginn;
    private LocalDateTime endOfStopp;
    private String marktId;
    //private Long routeId;

    @ManyToOne
    //@JoinColumn(name = "route_id", nullable = false)
    private Route route;

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getBeginn() {
        return beginn;
    }

    public void setBeginn(LocalDateTime beginn) {
        this.beginn = beginn;
    }

    public LocalDateTime getEndOfStopp() {
        return endOfStopp;
    }

    public void setEndOfStopp(LocalDateTime endOfStopp) {
        this.endOfStopp = endOfStopp;
    }

    public String getMarktId() {
        return marktId;
    }

    public void setMarktId(String marktId) {
        this.marktId = marktId;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stop stop)) return false;

        return Objects.equals(getId(), stop.getId()) && Objects.equals(getBeginn(), stop.getBeginn()) && Objects.equals(getEndOfStopp(), stop.getEndOfStopp()) && Objects.equals(getMarktId(), stop.getMarktId());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getId());
        result = 31 * result + Objects.hashCode(getBeginn());
        result = 31 * result + Objects.hashCode(getEndOfStopp());
        result = 31 * result + Objects.hashCode(getMarktId());
        return result;
    }
}