package com.gca.mapper;

import com.gca.dto.training.TrainingCreateRequest;
import com.gca.dto.training.TrainingDTO;
import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.Training;
import com.gca.model.TrainingType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TrainingMapperTest {
    private static final Long TRAINER_ID = 1L;
    private static final Long TRAINEE_ID = 2L;
    private static final Long TYPE_ID = 7L;
    private static final String TYPE_NAME = "Functional";
    private static final String TYPE_NAME_YOGA = "Yoga";

    TrainingMapper mapper = new TrainingMapperImpl();

    @Test
    void testToEntity_fromCreateRequest() {
        TrainingCreateRequest request = TrainingCreateRequest.builder()
                .trainerId(TRAINER_ID)
                .traineeId(TRAINEE_ID)
                .trainingTypeId(TYPE_ID)
                .date(LocalDate.of(2025, 6, 28))
                .duration(90L)
                .name(TYPE_NAME)
                .build();

        Training actual = mapper.toEntity(request);

        assertEquals(LocalDate.of(2025, 6, 28), actual.getDate());
        assertEquals(90L, actual.getDuration());
        assertEquals(TYPE_NAME, actual.getName());
    }

    @Test
    void testToResponse() {
        TrainingType type = buildTrainingType();

        Trainer trainer = Trainer.builder().id(TRAINER_ID).build();
        Trainee trainee = Trainee.builder().id(TRAINEE_ID).build();

        Training training = Training.builder()
                .id(10L)
                .trainer(trainer)
                .trainee(trainee)
                .type(type)
                .date(LocalDate.of(2025, 6, 29))
                .duration(120L)
                .name("Morning Yoga")
                .build();

        TrainingDTO actual = mapper.toResponse(training);

        assertNotNull(actual);
        assertEquals(10L, actual.getId());
        assertEquals(LocalDate.of(2025, 6, 29), actual.getDate());
        assertEquals(120L, actual.getDuration());
        assertEquals("Morning Yoga", actual.getName());
    }

    private static TrainingType buildTrainingType() {
        return TrainingType.builder()
                .id(TYPE_ID)
                .name(TYPE_NAME_YOGA)
                .build();
    }
}
