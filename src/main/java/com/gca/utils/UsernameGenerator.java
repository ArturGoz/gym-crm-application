package com.gca.utils;

import com.gca.dao.TraineeDAO;
import com.gca.dao.TrainerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsernameGenerator {
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
        String base = firstName + "." + lastName;
        String candidate = base;
        int suffix = 1;
        while (usernameExists(candidate)) {
            candidate = base + suffix++;
        }
        return candidate;
    }

    private boolean usernameExists(String username) {
        return traineeDAO.getByUsername(username) != null ||
                trainerDAO.getByUsername(username) != null;
    }
}
