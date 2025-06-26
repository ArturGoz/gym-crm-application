package com.gca.storage;

import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.Training;
import com.gca.model.TrainingType;
import com.gca.model.User;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Map;

@Component
@Data
public class StorageInitializer {
    @Value("${storage.init.file}")
    private String initFilePath;
    private static final Logger logger = LoggerFactory.getLogger(StorageInitializer.class);
    private Map<Long, Training> trainingStorage;
    private Map<Long, Trainer> trainerStorage;
    private Map<Long, Trainee> traineeStorage;

    @Autowired
    public void setTrainingStorage(Map<Long, Training> trainingStorage) {
        this.trainingStorage = trainingStorage;
    }

    @Autowired
    public void setTrainerStorage(Map<Long, Trainer> trainerStorage) {
        this.trainerStorage = trainerStorage;
    }

    @Autowired
    public void setTraineeStorage(Map<Long, Trainee> traineeStorage) {
        this.traineeStorage = traineeStorage;
    }

    @PostConstruct
    public void init() {
        logger.info("Init path file: {}", initFilePath);

        try {
            processInitFile(initFilePath);
            logger.info("Storage initialized successfully.");
        } catch (IOException e) {
            logger.error("Failed to initialize data from file: {}", initFilePath);
            throw new RuntimeException("Failed to initialize data from file: " + initFilePath, e);
        }
    }

    private void processInitFile(String filePath) throws IOException {
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

                handleDataLine(currentSection, line);
            }
        }
    }

    private boolean isSectionHeader(String line) {
        return line.startsWith("[") && line.endsWith("]");
    }

    private String extractSectionName(String line) {
        return line.substring(1, line.length() - 1).trim();
    }

    private void handleDataLine(String section, String line) {
        switch (section) {
            case "Training" -> parseAndAddTraining(line);
            case "Trainer" -> parseAndAddTrainer(line);
            case "Trainee" -> parseAndAddTrainee(line);
            default -> throw new IllegalArgumentException("Unknown section: " + section);
        }
    }

    private void parseAndAddTraining(String line) {
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

        trainingStorage.put(id, training);
    }

    private void parseAndAddTrainer(String line) {
        Trainer trainer = new Trainer();
        String[] parts = line.split(",");

        Long trainerId = parseAndSetUserPartsAndReturnUserId(trainer, parts);
        String specialization = parts[6];
        trainer.setSpecialization(specialization);

        trainerStorage.put(trainerId, trainer);
    }

    private void parseAndAddTrainee(String line) {
        Trainee trainee = new Trainee();
        String[] parts = line.split(",");

        Long traineeId = parseAndSetUserPartsAndReturnUserId(trainee, parts);
        LocalDate dateOfBirth = LocalDate.parse(parts[6]); // Очікується формат "yyyy-MM-dd"
        String address = parts[7];
        trainee.setDateOfBirth(dateOfBirth);
        trainee.setAddress(address);

        traineeStorage.put(traineeId, trainee);
    }

    private Long parseAndSetUserPartsAndReturnUserId(User user, String[] parts){
        Long id = Long.parseLong(parts[0]);
        String firstName = parts[1];
        String lastName = parts[2];
        String username = parts[3];
        String password = parts[4];
        boolean isActive = Boolean.parseBoolean(parts[5]);

        user.setUserId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setPassword(password);
        user.setActive(isActive);
        return id;
    }
}
