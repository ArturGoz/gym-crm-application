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

    private User buildUser(Long userId, String username, String firstName, String lastName, boolean isActive) {
        return User.builder()
                .id(userId)
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .isActive(isActive)
                .build();
    }

    private TrainingType buildTrainingType(String name) {
        return TrainingType.builder()
                .id(STATIC_SPECIALIZATION_ID)
                .name(name)
                .build();
    }

    @Test
    void testToEntity_fromCreateRequest() {
        TrainerCreateRequest request = TrainerCreateRequest.builder()
                .userId(STATIC_USER_ID)
                .specialization(buildTrainingType("Crossfit"))
                .build();

        Trainer entity = mapper.toEntity(request);

        assertNotNull(entity);
        assertNotNull(entity.getUser());
        assertEquals(STATIC_USER_ID, entity.getUser().getId());
        assertEquals("Crossfit", entity.getSpecialization().getName());
    }

    @Test
    void testToEntity_fromUpdateRequest() {
        TrainerUpdateRequest request = TrainerUpdateRequest.builder()
                .id(20L)
                .specialization(buildTrainingType("Yoga"))
                .build();

        Trainer entity = mapper.toEntity(request);

        assertNotNull(entity);
        assertEquals(20L, entity.getId());
        assertEquals("Yoga", entity.getSpecialization().getName());
    }

    @Test
    void testToResponse() {
        User user = buildUser(STATIC_USER_ID, STATIC_USERNAME, STATIC_FIRSTNAME, STATIC_LASTNAME, STATIC_IS_ACTIVE);
        Trainer entity = Trainer.builder()
                .id(100L)
                .user(user)
                .specialization(buildTrainingType(STATIC_SPECIALIZATION_NAME))
                .trainings(Collections.emptyList())
                .trainees(Collections.emptySet())
                .build();

        TrainerResponse response = mapper.toResponse(entity);

        assertNotNull(response);
        assertEquals(100L, response.getId());
        assertEquals(STATIC_USER_ID, response.getUserId());
        assertEquals(STATIC_SPECIALIZATION_ID, response.getSpecialization().getId());
        assertEquals(STATIC_SPECIALIZATION_NAME, response.getSpecialization().getName());
    }
}
