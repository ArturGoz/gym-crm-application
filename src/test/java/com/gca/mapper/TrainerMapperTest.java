package com.gca.mapper;

import com.gca.dto.trainer.TrainerCreateRequest;
import com.gca.dto.trainer.TrainerResponse;
import com.gca.dto.trainer.TrainerUpdateRequest;
import com.gca.model.Trainer;
import com.gca.model.TrainingType;
import com.gca.model.User;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TrainerMapperTest {
    private static final Long USER_ID = 30L;
    private static final String USERNAME = "alice.smith";
    private static final String FIRSTNAME = "Alice";
    private static final String LASTNAME = "Smith";
    private static final boolean IS_ACTIVE = true;
    private static final Long SPECIALIZATION_ID = 42L;
    private static final String SPECIALIZATION_NAME = "Pilates";

    TrainerMapper mapper = new TrainerMapperImpl();

    @Test
    void testToEntity_fromCreateRequest() {
        TrainerCreateRequest request = TrainerCreateRequest.builder()
                .userId(USER_ID)
                .specialization(buildTrainingType("Crossfit"))
                .build();

        Trainer actual = mapper.toEntity(request);

        assertNotNull(actual);
        assertEquals("Crossfit", actual.getSpecialization().getName());
    }

    @Test
    void testToEntity_fromUpdateRequest() {
        TrainerUpdateRequest request = TrainerUpdateRequest.builder()
                .id(20L)
                .specialization(buildTrainingType("Yoga"))
                .build();

        Trainer actual = mapper.toEntity(request);

        assertNotNull(actual);
        assertEquals(20L, actual.getId());
        assertEquals("Yoga", actual.getSpecialization().getName());
    }

    @Test
    void testToResponse() {
        User user = buildUser();
        Trainer entity = Trainer.builder()
                .id(100L)
                .user(user)
                .specialization(buildTrainingType(SPECIALIZATION_NAME))
                .trainings(Collections.emptyList())
                .trainees(Collections.emptySet())
                .build();

        TrainerResponse actual = mapper.toResponse(entity);

        assertNotNull(actual);
        assertEquals(100L, actual.getId());
        assertEquals(SPECIALIZATION_ID, actual.getSpecialization().getId());
        assertEquals(SPECIALIZATION_NAME, actual.getSpecialization().getName());
    }

    private User buildUser() {
        return User.builder()
                .id(USER_ID)
                .username(USERNAME)
                .firstName(FIRSTNAME)
                .lastName(LASTNAME)
                .isActive(IS_ACTIVE)
                .build();
    }

    private TrainingType buildTrainingType(String name) {
        return TrainingType.builder()
                .id(SPECIALIZATION_ID)
                .name(name)
                .build();
    }
}
