package com.example.Bloodline_ADN_System.Entity;

import jakarta.persistence.*;

@Entity
public class AllowedArea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String city;      // Thành phố/Tỉnh

    @Column(nullable = false)
    private String district;  // Quận/Huyện

    @Column(nullable = false)
    private Boolean active = true; // Có đang cho phép lấy mẫu không

    private String note;      // Ghi chú (tùy chọn)

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
} 