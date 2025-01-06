package pl.wp.workdayrecorder2024_ver1.model;

import jakarta.persistence.*;

@Entity
public class Signature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@Lob
    //private String signatureData; // przechowuje podpis w formacie base64

    @Lob
    private byte[] imageData;

    // Gettery i Settery
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }
}
