
package com.gca.repository.impl;

import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.Training;
import com.gca.model.TrainingType;
import com.gca.model.User;
import com.gca.repository.BaseIntegrationTest;
import com.gca.repository.TrainingRepository;
import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataSet(value = "dataset/training/training-data.xml", cleanBefore = true, cleanAfter = true, transactional = true)
public class TrainingRepositoryTest extends BaseIntegrationTest<TrainingRepository> {

    @Test
    @DataSet(value = "dataset/training/training-creation-data.xml", cleanBefore = true, cleanAfter = true, transactional = true)
    void shouldCreateTraining() {
        Training expected = buildTrainingFromExistingData();

        Training actual = repository.save(expected);

        assertNotNull(actual.getId(), "Created training should have ID");
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDuration(), actual.getDuration());
        assertEquals(expected.getDate(), actual.getDate());
        assertEquals(expected.getTrainer().getId(), actual.getTrainer().getId());
        assertEquals(expected.getTrainee().getId(), actual.getTrainee().getId());
        assertEquals(expected.getType().getId(), actual.getType().getId());
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

        List<Training> result = repository.getTrainerTrainings(trainer, fromDate, toDate, traineeName);

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

        List<Training> result = repository.getTraineeTrainings(trainee, fromDate, toDate, trainerName, trainingTypeName);

        assertEquals(expectedCount, result.size(), description);
    }

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
                Arguments.of("Filter by existing trainee name", null, null, "arnold.schwarzenegger", 2),
                Arguments.of("Filter by non-existing trainee name", null, null, "nonexistent", 0),
                Arguments.of("Filter by date range matching one", LocalDate.of(2025, 7, 7), LocalDate.of(2025, 7, 7), null, 1),
                Arguments.of("Filter by date range not matching", LocalDate.of(2025, 7, 1), LocalDate.of(2025, 7, 6), null, 0)
        );
    }

    private Training buildTrainingFromExistingData() {
        return Training.builder()
                .name("New Training Session")
                .date(LocalDate.of(2025, 7, 8))
                .duration(90L)
                .trainee(buildTrainee(buildUserArnold()))
                .trainer(buildTrainer(buildUserTrainerOne(), buildTrainingType()))
                .type(buildTrainingType())
                .build();
    }

    private User buildUserArnold() {
        return User.builder()
                .id(1L)
                .firstName("Arnold")
                .lastName("Schwarzenegger")
                .username("arnold.schwarzenegger")
                .password("test123")
                .isActive(true)
                .build();
    }

    private Trainee buildTrainee(User user) {
        return Trainee.builder()
                .id(1L)
                .user(user)
                .address("123 Main St")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .build();
    }

    private TrainingType buildTrainingType() {
        return TrainingType.builder()
                .id(1L)
                .name("Fitness")
                .build();
    }

    private User buildUserTrainerOne() {
        return User.builder()
                .id(2L)
                .firstName("Trainer")
                .lastName("One")
                .username("trainer.one")
                .password("trainerpass")
                .isActive(true)
                .build();
    }

    private Trainer buildTrainer(User user, TrainingType specialization) {
        return Trainer.builder()
                .id(1L)
                .user(user)
                .specialization(specialization)
                .build();
    }
}

