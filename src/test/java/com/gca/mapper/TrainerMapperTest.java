package com.gca.mapper;

import com.gca.dto.trainer.TrainerCreateRequest;
import com.gca.dto.trainer.TrainerResponse;
import com.gca.dto.trainer.TrainerUpdateRequest;
import com.gca.model.Trainer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrainerMapperTest {
    TrainerMapper mapper = new TrainerMapperImpl();

    @Test
    void testToEntity_fromCreateRequest() {
        TrainerCreateRequest request = TrainerCreateRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .specialization("Crossfit")
                .build();

        Trainer entity = mapper.toEntity(request);

        assertEquals("John", entity.getFirstName());
        assertEquals("Doe", entity.getLastName());
        assertEquals("Crossfit", entity.getSpecialization());
    }

    @Test
    void testToEntity_fromUpdateRequest() {
        TrainerUpdateRequest request = TrainerUpdateRequest.builder()
                .userId(20L)
                .isActive(false)
                .specialization("Yoga")
                .build();

        Trainer entity = mapper.toEntity(request);

        assertEquals(20L, entity.getUserId());
        assertFalse(entity.isActive());
        assertEquals("Yoga", entity.getSpecialization());
    }

    @Test
    void testToResponse() {
        Trainer entity = Trainer.builder()
                .userId(30L)
                .firstName("Alice")
                .lastName("Smith")
                .username("alice.smith")
                .isActive(true)
                .specialization("Pilates")
                .build();

        TrainerResponse response = mapper.toResponse(entity);

        assertEquals(30L, response.getUserId());
        assertEquals("Alice", response.getFirstName());
        assertEquals("Smith", response.getLastName());
        assertEquals("alice.smith", response.getUsername());
        assertTrue(response.isActive());
        assertEquals("Pilates", response.getSpecialization());
    }
}
