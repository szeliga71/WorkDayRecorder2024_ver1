package pl.wp.workdayrecorder2024_ver1.model;

import jakarta.persistence.*;

@Entity
public class Markt {
    @Id
    private String marktId;
    private String name;
    private String postalCode;
    private String citi;
    private String street;
    private String buildingNumber;
    @Column(length = 1000)
    private String notes;


    public String getMarktId() {
        return marktId;
    }

    public void setMarktId(String marktId) {
        this.marktId = marktId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCiti() {
        return citi;
    }

    public void setCiti(String citi) {
        this.citi = citi;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(String buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
