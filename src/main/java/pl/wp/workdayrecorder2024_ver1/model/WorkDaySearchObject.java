package pl.wp.workdayrecorder2024_ver1.model;


public class WorkDaySearchObject {

    private String personalId;
    ;
    private String dayOfWeek;
    private Integer KW;

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

    public Integer getKW() {
        return KW;
    }

    public void setKW(Integer KW) {
        this.KW = KW;
    }
}