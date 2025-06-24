package com.gca.storage;

import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.Training;
import com.gca.model.TrainingType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.HashMap;
import java.util.Map;

@Configuration
@PropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
public class StorageConfig {
    @Bean
    public Map<Long, Training> trainingStorage() {
        return new HashMap<>();
    }

    @Bean
    public Map<Long, Trainer> trainerStorage() {
        return new HashMap<>();
    }

    @Bean
    public Map<Long, Trainee> traineeStorage() {
        return new HashMap<>();
    }
}