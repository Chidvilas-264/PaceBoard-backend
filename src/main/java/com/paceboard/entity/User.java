package com.paceboard.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Basic Info
    private String name;
    private String username;
    private String phone;
    private String email;
    private String password;

    // Questionnaire Data
    private Integer age;
    private String gender;
    private Double height;
    private Double weight;
    private String locality;
    private String preferredActivity;

    // Dashboard Data
    private Integer dailyStepGoal = 10000;
    private Integer monthlySteps = 0;
    private Integer fitnessStreak = 0;

    // Settings
    private String theme = "bright";

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_groups",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private java.util.Set<FitnessGroup> joinedGroups = new java.util.HashSet<>();

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public Double getHeight() { return height; }
    public void setHeight(Double height) { this.height = height; }
    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }
    public String getLocality() { return locality; }
    public void setLocality(String locality) { this.locality = locality; }
    public String getPreferredActivity() { return preferredActivity; }
    public void setPreferredActivity(String preferredActivity) { this.preferredActivity = preferredActivity; }
    public Integer getDailyStepGoal() { return dailyStepGoal; }
    public void setDailyStepGoal(Integer dailyStepGoal) { this.dailyStepGoal = dailyStepGoal; }
    public Integer getMonthlySteps() { return monthlySteps; }
    public void setMonthlySteps(Integer monthlySteps) { this.monthlySteps = monthlySteps; }
    public Integer getFitnessStreak() { return fitnessStreak; }
    public void setFitnessStreak(Integer fitnessStreak) { this.fitnessStreak = fitnessStreak; }
    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }
    public java.util.Set<FitnessGroup> getJoinedGroups() { return joinedGroups; }
    public void setJoinedGroups(java.util.Set<FitnessGroup> joinedGroups) { this.joinedGroups = joinedGroups; }
}
