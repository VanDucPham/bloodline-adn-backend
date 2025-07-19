package com.example.Bloodline_ADN_System.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long userId;

        @Column(unique = true, nullable = false)
        private String email;

        @Column(nullable = false)
        private String password;

        private String citizenId;
        private String name;
        private String gender;
        private String phone;
        private String address;

        private LocalDate createAt;

        @PrePersist
        public void prePersist() {
                if (createAt == null) {
                        createAt = LocalDate.now();
                }
        }
        @Enumerated(EnumType.STRING)
        private Status status;

        private LocalDate birthDate;

        @Enumerated(EnumType.STRING)
        private UserRole role;

        // Staff-specific fields for home collection
        private String staffCode;          // Mã nhân viên


        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        private List<Appointment> appointments = new ArrayList<>();

        @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        private List<Blog> blogs = new ArrayList<>();

        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        private List<Feedback> feedbacks = new ArrayList<>();

        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        private List<Notification> notifications = new ArrayList<>();



    public enum UserRole {
                ADMIN, CUSTOMER, STAFF, MANAGER
        }

        public void setRole(UserRole role) {
                this.role = role;
        }

        public String getRole() {
                return this.role.toString();
        }

        public enum Status {
                ACTIVE, INACTIVE
        }

        public void setStatusFromString(String status) {
                this.status = Status.valueOf(status.toUpperCase());
        }

        public String getStatusString() {
                return this.status != null ? this.status.toString() : null;
        }
}
