package pl.wp.workdayrecorder2024_ver1.model;

import jakarta.persistence.*;

@Entity
public class Truck {

    @Id
    private String number;
    @Column(length = 1000)
    private String notes;

    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
