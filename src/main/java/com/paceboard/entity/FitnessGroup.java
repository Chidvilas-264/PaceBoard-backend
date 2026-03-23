package com.paceboard.entity;
import jakarta.persistence.*;

@Entity
@Table(name = "fitness_groups")
public class FitnessGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String locality;
    private String preferredActivity;
    private Integer totalMembers = 0;
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLocality() { return locality; }
    public void setLocality(String locality) { this.locality = locality; }
    public String getPreferredActivity() { return preferredActivity; }
    public void setPreferredActivity(String preferredActivity) { this.preferredActivity = preferredActivity; }
    public Integer getTotalMembers() { return totalMembers; }
    public void setTotalMembers(Integer totalMembers) { this.totalMembers = totalMembers; }
}
