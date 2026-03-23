package com.paceboard.repository;

import com.paceboard.entity.FitnessGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FitnessGroupRepository extends JpaRepository<FitnessGroup, Long> {
    List<FitnessGroup> findByLocality(String locality);
    List<FitnessGroup> findByPreferredActivity(String preferredActivity);
}
