package com.gca.storage;

import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.Training;
import com.gca.model.TrainingType;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.Duration;
import java.time.LocalDate;
import java.util.function.Supplier;

@Component
@Data
public class StorageInitializer {
    private static final Logger logger = LoggerFactory.getLogger(StorageInitializer.class);

    @Value("${storage.init.file}")
    private String initFilePath;

    private Supplier<Reader> readerSupplier;

    public StorageInitializer() {
        this.readerSupplier = () -> {
            try {
                return new InputStreamReader(new ClassPathResource(initFilePath).getInputStream());
            } catch (IOException e) {
                throw new RuntimeException("Failed to create reader for file: " + initFilePath, e);
            }
        };
    }

    public InitializedData initializeData() {
        logger.info("Initializing data from file: {}", initFilePath);

        try {
            InitializedData data = initializeDataFromFile(initFilePath);
            logger.info("Data initialized successfully.");
            return data;
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize data from file: " + initFilePath, e);
        }
    }

    private InitializedData initializeDataFromFile(String filePath) throws IOException {
        InitializedData data = new InitializedData();

        try (BufferedReader reader = new BufferedReader(readerSupplier.get())) {
            String currentSection = null;
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                if (isSectionHeader(line)) {
                    currentSection = extractSectionName(line);
                    continue;
                }
                handleDataLine(currentSection, line, data);
            }
        }

        return data;
    }

    private void handleDataLine(String section, String line, InitializedData data) {
        switch (section) {
            case "Training" -> data.getTrainings().add(parseTraining(line));
            case "Trainer" -> data.getTrainers().add(parseTrainer(line));
            case "Trainee" -> data.getTrainees().add(parseTrainee(line));
            default -> throw new IllegalArgumentException("Unknown section: " + section);
        }
    }

    private boolean isSectionHeader(String line) {
        return line.startsWith("[") && line.endsWith("]");
    }

    private String extractSectionName(String line) {
        return line.substring(1, line.length() - 1).trim();
    }

    private Training parseTraining(String line) {
        String[] parts = line.split(",");

        Long id = Long.parseLong(parts[0]);
        Long trainerId = Long.parseLong(parts[1]);
        Long traineeId = Long.parseLong(parts[2]);
        LocalDate trainingDate = LocalDate.parse(parts[3]);
        Duration trainingDuration = Duration.parse(parts[4]);
        String trainingName = parts[5];
        String trainingTypeStr = parts[6];
        TrainingType trainingType = new TrainingType(trainingTypeStr);

        return Training.builder()
                .id(id)
                .trainerId(trainerId)
                .traineeId(traineeId)
                .trainingDate(trainingDate)
                .trainingDuration(trainingDuration)
                .trainingName(trainingName)
                .trainingType(trainingType)
                .build();
    }

    private Trainer parseTrainer(String line) {
        String[] parts = line.split(",");

        return Trainer.builder()
                .userId(Long.parseLong(parts[0]))
                .firstName(parts[1])
                .lastName(parts[2])
                .username(parts[3])
                .password(parts[4])
                .isActive(Boolean.parseBoolean(parts[5]))
                .specialization(parts[6])
                .build();
    }

    private Trainee parseTrainee(String line) {
        String[] parts = line.split(",");

        return Trainee.builder()
                .userId(Long.parseLong(parts[0]))
                .firstName(parts[1])
                .lastName(parts[2])
                .username(parts[3])
                .password(parts[4])
                .isActive(Boolean.parseBoolean(parts[5]))
                .dateOfBirth(LocalDate.parse(parts[6]))
                .address(parts[7])
                .build();
    }
}
