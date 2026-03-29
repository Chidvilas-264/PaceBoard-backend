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
    private String activeSince;
    private Long creatorId;
    
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
    public String getActiveSince() { return activeSince; }
    public void setActiveSince(String activeSince) { this.activeSince = activeSince; }
    public Long getCreatorId() { return creatorId; }
    public void setCreatorId(Long creatorId) { this.creatorId = creatorId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FitnessGroup that = (FitnessGroup) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
