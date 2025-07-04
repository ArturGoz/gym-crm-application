package com.gca.mapper;

import com.gca.dto.training.TrainingCreateRequest;
import com.gca.dto.training.TrainingResponse;
import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.Training;
import com.gca.model.TrainingType;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TrainingMapperTest {
    TrainingMapper mapper = new TrainingMapperImpl();

    private static final Long STATIC_TRAINER_ID = 1L;
    private static final Long STATIC_TRAINEE_ID = 2L;
    private static final Long STATIC_TYPE_ID = 7L;
    private static final String STATIC_TYPE_NAME = "Functional";
    private static final String STATIC_TYPE_NAME_YOGA = "Yoga";

    private static TrainingType buildTrainingType(Long id, String name) {
        return TrainingType.builder()
                .id(id)
                .name(name)
                .build();
    }

    @Test
    void testToEntity_fromCreateRequest() {
        TrainingCreateRequest request = TrainingCreateRequest.builder()
                .trainerId(STATIC_TRAINER_ID)
                .traineeId(STATIC_TRAINEE_ID)
                .trainingTypeId(STATIC_TYPE_ID)
                .trainingDate(LocalDate.of(2025, 6, 28))
                .trainingDuration(90L)
                .trainingName("Functional Training")
                .build();

        Training entity = mapper.toEntity(request);

        assertNotNull(entity);
        assertNotNull(entity.getTrainer());
        assertNotNull(entity.getTrainee());
        assertEquals(STATIC_TRAINER_ID, entity.getTrainer().getId());
        assertEquals(STATIC_TRAINEE_ID, entity.getTrainee().getId());

        assertNotNull(entity.getType());
        assertEquals(STATIC_TYPE_ID, entity.getType().getId());

        assertEquals(LocalDate.of(2025, 6, 28), entity.getDate());
        assertEquals(90L, entity.getDuration());
        assertEquals("Functional Training", entity.getName());
    }

    @Test
    void testToResponse() {
        TrainingType type = buildTrainingType(STATIC_TYPE_ID, STATIC_TYPE_NAME_YOGA);

        Trainer trainer = Trainer.builder().id(STATIC_TRAINER_ID).build();
        Trainee trainee = Trainee.builder().id(STATIC_TRAINEE_ID).build();

        Training training = Training.builder()
                .id(10L)
                .trainer(trainer)
                .trainee(trainee)
                .type(type)
                .date(LocalDate.of(2025, 6, 29))
                .duration(120L)
                .name("Morning Yoga")
                .build();

        TrainingResponse response = mapper.toResponse(training);

        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals(STATIC_TRAINER_ID, response.getTrainerId());
        assertEquals(STATIC_TRAINEE_ID, response.getTraineeId());
        assertEquals(STATIC_TYPE_ID, response.getTrainingTypeId());
        assertEquals(LocalDate.of(2025, 6, 29), response.getTrainingDate());
        assertEquals(120L, response.getTrainingDuration());
        assertEquals("Morning Yoga", response.getTrainingName());
        assertNotNull(response.getTrainingType());
        assertEquals(STATIC_TYPE_NAME_YOGA, response.getTrainingType().getName());
    }
}
