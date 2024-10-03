package pl.wp.workdayrecorder2024_ver1.model;

import java.time.LocalDateTime;

public class WorkDaySearchObject {

    private String personalId;
    private LocalDateTime date;

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
