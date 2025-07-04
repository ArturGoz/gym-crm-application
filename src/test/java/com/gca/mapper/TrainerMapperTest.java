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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrainerMapperTest {
    TrainerMapper mapper = new TrainerMapperImpl();

    private static final Long STATIC_USER_ID = 30L;
    private static final String STATIC_USERNAME = "alice.smith";
    private static final String STATIC_FIRSTNAME = "Alice";
    private static final String STATIC_LASTNAME = "Smith";
    private static final boolean STATIC_IS_ACTIVE = true;

    private static final Long STATIC_SPECIALIZATION_ID = 42L;
    private static final String STATIC_SPECIALIZATION_NAME = "Pilates";

    @Test
    void testToEntity_fromCreateRequest() {
        TrainerCreateRequest request = TrainerCreateRequest.builder()
                .userId(STATIC_USER_ID)
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
                .specialization(buildTrainingType(STATIC_SPECIALIZATION_NAME))
                .trainings(Collections.emptyList())
                .trainees(Collections.emptySet())
                .build();

        TrainerResponse actual = mapper.toResponse(entity);

        assertNotNull(actual);
        assertEquals(100L, actual.getId());
        assertEquals(STATIC_SPECIALIZATION_ID, actual.getSpecialization().getId());
        assertEquals(STATIC_SPECIALIZATION_NAME, actual.getSpecialization().getName());
    }

    private User buildUser() {
        return User.builder()
                .id(STATIC_USER_ID)
                .username(STATIC_USERNAME)
                .firstName(STATIC_FIRSTNAME)
                .lastName(STATIC_LASTNAME)
                .isActive(STATIC_IS_ACTIVE)
                .build();
    }

    private TrainingType buildTrainingType(String name) {
        return TrainingType.builder()
                .id(STATIC_SPECIALIZATION_ID)
                .name(name)
                .build();
    }
}
