package com.gca.utils;

import com.gca.dao.TraineeDAO;
import com.gca.dao.TrainerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CombinedUsernameExistenceChecker {
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

    public boolean exists(String username) {
        return traineeDAO.getByUsername(username) != null || trainerDAO.getByUsername(username) != null;
    }
}
