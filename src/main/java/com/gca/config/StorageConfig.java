package com.gca.config;

import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.Training;
import com.gca.storage.Namespace;
import com.gca.storage.StorageRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

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
    public StorageRegistry storageRegistry() {
        return new StorageRegistry();
    }

    @Bean
    public Map<Long, Training> trainingStorage(StorageRegistry registry) {
        return registry.getStorage(Namespace.TRAINING);
    }

    @Bean
    public Map<Long, Trainer> trainerStorage(StorageRegistry registry) {
        return registry.getStorage(Namespace.TRAINER);
    }

    @Bean
    public Map<Long, Trainee> traineeStorage(StorageRegistry registry) {
        return registry.getStorage(Namespace.TRAINEE);
    }
}
