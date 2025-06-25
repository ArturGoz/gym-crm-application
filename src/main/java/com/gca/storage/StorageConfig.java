package com.gca.storage;

import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.Training;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import java.util.HashMap;
import java.util.Map;


@Configuration
@PropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
public class StorageConfig {
    private static final Map<Namespace, Map<Long, ?>> storageMap;

    static
    {
        storageMap = new HashMap<>();
        storageMap.put(Namespace.TRAINING, new HashMap<Long, Training>());
        storageMap.put(Namespace.TRAINER, new HashMap<Long, Trainer>());
        storageMap.put(Namespace.TRAINEE, new HashMap<Long, Trainee>());
    }

    @Bean
    @SuppressWarnings("unchecked")
    public Map<Long, Training> trainingStorage() {
        return (Map<Long, Training>) storageMap.get(Namespace.TRAINING);
    }

    @Bean
    @SuppressWarnings("unchecked")
    public Map<Long, Trainer> trainerStorage() {
        return (Map<Long, Trainer>) storageMap.get(Namespace.TRAINER);
    }

    @Bean
    @SuppressWarnings("unchecked")
    public Map<Long, Trainee> traineeStorage() {
        return (Map<Long, Trainee>) storageMap.get(Namespace.TRAINEE);
    }
}