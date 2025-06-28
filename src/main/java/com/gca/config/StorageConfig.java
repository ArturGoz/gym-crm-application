package com.gca.config;

import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.Training;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan(basePackages = "com.gca.storage")
@PropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
public class StorageConfig {
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

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
