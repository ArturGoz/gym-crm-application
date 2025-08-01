package com.gca.service.integration;

import com.gca.dao.TraineeDAO;
import com.gca.dao.TrainerDAO;
import com.gca.dao.TrainingDAO;
import com.gca.dao.TrainingTypeDAO;
import com.gca.dto.training.TrainingCreateDTO;
import com.gca.dto.training.TrainingDTO;
import com.gca.mapper.TrainingMapper;
import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.TrainingType;
import com.gca.service.impl.TrainingServiceImpl;
import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrainingServiceIT extends AbstractServiceIT {
    @Autowired
    private TrainingDAO dao;

    @Autowired
    private TrainerDAO trainerDAO;

    @Autowired
    private TraineeDAO traineeDAO;

    @Autowired
    private TrainingTypeDAO trainingTypeDAO;

    private TrainingServiceImpl trainingService;

    @BeforeEach
    void setUp() {
        transaction = sessionFactory.getCurrentSession().beginTransaction();

        TrainingMapper trainingMapper = Mappers.getMapper(TrainingMapper.class);

        trainingService = new TrainingServiceImpl();
        trainingService.setTrainingDAO(dao);
        trainingService.setTrainerDAO(trainerDAO);
        trainingService.setTraineeDAO(traineeDAO);
        trainingService.setTrainingTypeDAO(trainingTypeDAO);
        trainingService.setTrainingMapper(trainingMapper);
    }

    @AfterEach
    void tearDown() {
        if (transaction != null && transaction.isActive()) {
            transaction.rollback();
        }
    }

    @Test
    @DataSet(value = "dataset/training/training-data.xml", cleanBefore = true, cleanAfter = true, transactional = true)
    void shouldCreateTraining() {
        Trainer trainer = trainerDAO.getById(1L);
        Trainee trainee = traineeDAO.getById(1L);
        TrainingType type = trainingTypeDAO.getById(1L);

        TrainingCreateDTO request = TrainingCreateDTO.builder()
                .trainerUsername(trainer.getUser().getUsername())
                .traineeUsername(trainee.getUser().getUsername())
                .trainingName(type.getName())
                .trainingDate(LocalDate.of(2025, 7, 10))
                .duration(60L)
                .build();

        TrainingDTO actual = trainingService.createTraining(request);

        assertEquals(type.getName(), actual.getTrainingName());
        assertEquals(trainer.getUser().getUsername(), actual.getTrainerName());
        assertEquals(trainee.getUser().getUsername(), actual.getTraineeName());
    }
}
