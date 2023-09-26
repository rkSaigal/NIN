package com.example.jwt.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_profile")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    private String gender;
    private Date dateOfBirth;

    private double height;
    private double weight;
    private double bmi;


    // Constructors, getters, and setters


    // Uncomment this line to establish a one-to-one relationship with User
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

//    @OneToOne(mappedBy = "userProfile")
//    private FitnessActivity fitnessActivity;


}
