package com.paceboard.config;

import com.paceboard.entity.FitnessGroup;
import com.paceboard.repository.FitnessGroupRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SeedConfig {

    @Bean
    CommandLineRunner initDatabase(FitnessGroupRepository groupRepo) {
        return args -> {
            if (groupRepo.count() == 0) {
                FitnessGroup g1 = new FitnessGroup();
                g1.setName("Downtown Runners");
                g1.setLocality("Downtown");
                g1.setPreferredActivity("Run");
                g1.setTotalMembers(120);

                FitnessGroup g2 = new FitnessGroup();
                g2.setName("Yoga for Life");
                g2.setLocality("Westside");
                g2.setPreferredActivity("Yoga");
                g2.setTotalMembers(85);

                FitnessGroup g3 = new FitnessGroup();
                g3.setName("Morning Walkers");
                g3.setLocality("Downtown");
                g3.setPreferredActivity("Walk");
                g3.setTotalMembers(250);

                groupRepo.saveAll(List.of(g1, g2, g3));
            }
        };
    }
}
