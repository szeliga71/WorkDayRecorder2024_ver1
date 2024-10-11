package pl.wp.workdayrecorder2024_ver1.model;

import java.time.LocalDateTime;

public class WorkDaySearchObject {

    private String personalId;;
    private String dayOfWeek;
    private int KW;

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getKW() {
        return KW;
    }

    public void setKW(int KW) {
        this.KW = KW;
    }
}
