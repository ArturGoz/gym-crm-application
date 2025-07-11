package com.gca.service.integration;

import com.gca.dao.TraineeDAO;
import com.gca.dao.TrainerDAO;
import com.gca.dao.TrainingDAO;
import com.gca.dao.TrainingTypeDAO;
import com.gca.dto.training.TrainingCreateRequest;
import com.gca.dto.training.TrainingResponse;
import com.gca.mapper.TrainingMapper;
import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.Training;
import com.gca.model.TrainingType;
import com.gca.service.impl.TrainingServiceImpl;
import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

        TrainingCreateRequest request = TrainingCreateRequest.builder()
                .trainerId(trainer.getId())
                .traineeId(trainee.getId())
                .trainingTypeId(type.getId())
                .name("New Training")
                .date(LocalDate.of(2025, 7, 10))
                .duration(60L)
                .build();

        TrainingResponse actual = trainingService.createTraining(request);

        assertNotNull(actual.getId());
        assertEquals("New Training", actual.getName());
        assertEquals(trainer.getId(), actual.getTrainerId());
        assertEquals(trainee.getId(), actual.getTraineeId());
        assertEquals(type.getId(), actual.getTrainingTypeId());
    }

    @Test
    @DataSet(value = "dataset/training/training-data.xml", cleanBefore = true, cleanAfter = true, transactional = true)
    void shouldGetTrainingById() {
        TrainingResponse actual = trainingService.getTrainingById(1L);

        assertNotNull(actual);
        assertEquals(1L, actual.getId());
        assertEquals("Morning Workout", actual.getName());
        assertEquals(1L, actual.getTrainerId());
        assertEquals(1L, actual.getTraineeId());
        assertEquals(1L, actual.getTrainingTypeId());
    }

    @Test
    @DataSet(value = "dataset/training/training-data.xml", cleanBefore = true, cleanAfter = true, transactional = true)
    void shouldFindTraineeTrainingsWithCriteria() {
        Trainee trainee = sessionFactory.getCurrentSession().find(Trainee.class, 1L);

        List<Training> result = dao.getTraineeTrainings(trainee, null, null, null, null);

        assertEquals(1, result.size(), "Should find one training for trainee");

        Training training = result.get(0);
        assertEquals("Morning Workout", training.getName());
        assertEquals(trainee.getId(), training.getTrainee().getId());

        List<Training> result2 = dao.getTraineeTrainings(trainee, null, null, "trainer.one", null);
        List<Training> result3 = dao.getTraineeTrainings(trainee, null, null, "wrong.name", null);
        List<Training> result4 = dao.getTraineeTrainings(trainee, null, null, null, "Fitness");

        assertEquals(1, result2.size(), "Should find one training for trainee and trainer name");
        assertTrue(result3.isEmpty(), "Should find no trainings for non-matching trainer name");
        assertEquals(1, result4.size(), "Should find training with training type Fitness");
    }

    @Test
    @DataSet(value = "dataset/training/training-data.xml", cleanBefore = true, cleanAfter = true, transactional = true)
    void shouldFindTrainerTrainingsWithCriteria() {
        Trainer trainer = sessionFactory.getCurrentSession().find(Trainer.class, 1L);

        List<Training> result = dao.getTrainerTrainings(trainer, null, null, null);

        assertEquals(1, result.size(), "Should find one training for trainer");

        Training training = result.get(0);

        assertEquals("Morning Workout", training.getName());
        assertEquals(trainer.getId(), training.getTrainer().getId());

        List<Training> result2 = dao.getTrainerTrainings(trainer, null, null, "john.doe");
        List<Training> result3 = dao.getTrainerTrainings(trainer, null, null, "nonexistent");

        assertEquals(1, result2.size(), "Should find one training for trainer and trainee name");
        assertTrue(result3.isEmpty(), "Should find no trainings for non-matching trainee name");
    }

    @Test
    @DataSet(value = "dataset/training/training-data.xml", cleanBefore = true, cleanAfter = true, transactional = true)
    void shouldFilterTrainingsByDateRange() {
        Trainee trainee = sessionFactory.getCurrentSession().find(Trainee.class, 1L);

        LocalDate fromDate = LocalDate.of(2025, 7, 6);
        LocalDate toDate = LocalDate.of(2025, 7, 8);

        List<Training> result = dao.getTraineeTrainings(trainee, fromDate, toDate, null, null);
        List<Training> result2 = dao.getTraineeTrainings(trainee, LocalDate.of(2025, 7, 1), LocalDate.of(2025, 7, 6), null, null);

        assertEquals(1, result.size(), "Should find training in date range");
        assertTrue(result2.isEmpty(), "Should find no training outside date range");
    }

}
