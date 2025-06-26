package com.gca.storage;

import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.Training;
import com.gca.model.TrainingType;
import com.gca.model.User;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.LocalDate;

@Component
@Data
public class StorageInitializer {
    private static final Logger logger = LoggerFactory.getLogger(StorageInitializer.class);

    @Value("${storage.init.file}")
    private String initFilePath;

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

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ClassPathResource(filePath).getInputStream()))) {
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

        Training training = new Training();
        training.setId(id);
        training.setTrainerId(trainerId);
        training.setTraineeId(traineeId);
        training.setTrainingDate(trainingDate);
        training.setTrainingDuration(trainingDuration);
        training.setTrainingName(trainingName);
        training.setTrainingType(trainingType);

        return training;
    }

    private Trainer parseTrainer(String line) {
        String[] parts = line.split(",");

        Trainer trainer = parseUser(parts, new Trainer());
        trainer.setSpecialization(parts[6]);

        return trainer;
    }

    private Trainee parseTrainee(String line) {
        String[] parts = line.split(",");

        Trainee trainee = parseUser(parts, new Trainee());
        trainee.setDateOfBirth(LocalDate.parse(parts[6]));
        trainee.setAddress(parts[7]);

        return trainee;
    }

    private <T extends User> T parseUser(String[] parts, T user) {
        user.setUserId(Long.parseLong(parts[0]));
        user.setFirstName(parts[1]);
        user.setLastName(parts[2]);
        user.setUsername(parts[3]);
        user.setPassword(parts[4]);
        user.setActive(Boolean.parseBoolean(parts[5]));

        return user;
    }
}
