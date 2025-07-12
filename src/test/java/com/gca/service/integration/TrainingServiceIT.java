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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

    static Stream<Arguments> provideTraineeTrainingCriteria() {
        return Stream.of(
                Arguments.of("All trainings for trainee", null, null, null, null, 2),
                Arguments.of("Filter by existing trainer name", null, null, "trainer.one", null, 2),
                Arguments.of("Filter by wrong trainer name", null, null, "nonexistent", null, 0),
                Arguments.of("Filter by training type", null, null, null, "Fitness", 1),
                Arguments.of("Filter by date range matching one", LocalDate.of(2025, 7, 7), LocalDate.of(2025, 7, 7), null, null, 1),
                Arguments.of("Filter by date range not matching", LocalDate.of(2025, 7, 1), LocalDate.of(2025, 7, 6), null, null, 0)
        );
    }

    static Stream<Arguments> provideTrainerTrainingCriteria() {
        return Stream.of(
                Arguments.of("All trainings for trainer", null, null, null, 2),
                Arguments.of("Filter by existing trainee name", null, null, "john.doe", 2),
                Arguments.of("Filter by non-existing trainee name", null, null, "nonexistent", 0),
                Arguments.of("Filter by date range matching one", LocalDate.of(2025, 7, 7), LocalDate.of(2025, 7, 7), null, 1),
                Arguments.of("Filter by date range not matching", LocalDate.of(2025, 7, 1), LocalDate.of(2025, 7, 6), null, 0)
        );
    }

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

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideTrainerTrainingCriteria")
    @DataSet(value = "dataset/training/training-criteria-data.xml", cleanBefore = true, cleanAfter = true, transactional = true)
    void shouldFilterTrainerTrainingsByCriteria(String description,
                                                LocalDate fromDate,
                                                LocalDate toDate,
                                                String traineeName,
                                                int expectedCount) {
        Trainer trainer = Trainer.builder().id(1L).build();

        List<Training> result = dao.getTrainerTrainings(trainer, fromDate, toDate, traineeName);

        assertEquals(expectedCount, result.size(), description);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideTraineeTrainingCriteria")
    @DataSet(value = "dataset/training/training-criteria-data.xml", cleanBefore = true, cleanAfter = true, transactional = true)
    void shouldFilterTraineeTrainingsByCriteria(String description,
                                                LocalDate fromDate,
                                                LocalDate toDate,
                                                String trainerName,
                                                String trainingTypeName,
                                                int expectedCount) {
        Trainee trainee = Trainee.builder().id(1L).build();

        List<Training> result = dao.getTraineeTrainings(trainee, fromDate, toDate, trainerName, trainingTypeName);

        assertEquals(expectedCount, result.size(), description);
    }
}
