package com.gca.service.impl;

import com.gca.dao.TrainerDAO;
import com.gca.model.Trainer;
import com.gca.service.TrainerService;
import com.gca.utils.PasswordUtils;
import com.gca.utils.UsernameGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerServiceImpl implements TrainerService {
    private static final Logger logger = LoggerFactory.getLogger(TrainerServiceImpl.class);

    @Autowired private TrainerDAO trainerDAO;
    @Autowired private UsernameGenerator usernameGenerator;

    @Override
    public Trainer createTrainer(Trainer trainer) {
        String username = usernameGenerator.generate(trainer.getFirstName(), trainer.getLastName());
        String password = PasswordUtils.generateRandomPassword();

        trainer.setUsername(username);
        trainer.setPassword(password);
        trainer.setActive(true);

        logger.info("Creating trainer: {}", username);
        return trainerDAO.create(trainer);
    }

    @Override
    public Trainer updateTrainer(Trainer trainer) {
        Trainer existing = trainerDAO.getById(trainer.getUserId());
        if (existing == null) {
            logger.error("Trainer not found for update: {}", trainer.getUserId());
            throw new RuntimeException("Trainer not found");
        }

        existing.setSpecialization(trainer.getSpecialization());
        existing.setActive(trainer.isActive());

        logger.info("Updating trainer: {}", existing.getUsername());
        return trainerDAO.update(existing);
    }

    @Override
    public Trainer getTrainerById(Long id) {
        return trainerDAO.getById(id);
    }

    @Override
    public Trainer getTrainerByUsername(String username) {
        return trainerDAO.getByUsername(username);
    }

    @Override
    public List<Trainer> getAllTrainers() {
        return trainerDAO.getAll();
    }
}
