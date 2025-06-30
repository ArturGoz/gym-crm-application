package com.gca.service.common;

import com.gca.dao.TraineeDAO;
import com.gca.dao.TrainerDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class UsernameGenerator {
    private static final Logger logger = LoggerFactory.getLogger(UsernameGenerator.class);
    private TraineeDAO traineeDAO;
    private TrainerDAO trainerDAO;

    @Autowired
    public void setTraineeDAO(TraineeDAO traineeDAO) {
        this.traineeDAO = traineeDAO;
    }

    @Autowired
    public void setTrainerDAO(TrainerDAO trainerDAO) {
        this.trainerDAO = trainerDAO;
    }

    public String generate(String firstName, String lastName) {
        logger.debug("Generating username");
        String base = firstName + "." + lastName;
        Set<String> allUsernames = new HashSet<>();
        allUsernames.addAll(traineeDAO.getAllUsernames());
        allUsernames.addAll(trainerDAO.getAllUsernames());

        String candidate = base;
        int suffix = 1;

        while (allUsernames.contains(candidate)) {
            candidate = base + suffix++;
        }

        if (!candidate.equalsIgnoreCase(base)) {
            logger.warn("User with username {} already exists, generated alternative username: {}", base, candidate);
        }

        return candidate;
    }
}
