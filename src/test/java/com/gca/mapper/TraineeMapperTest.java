package com.gca.mapper;

import com.gca.GymTestProvider;
import com.gca.dto.trainee.TraineeCreateDTO;
import com.gca.dto.trainee.TraineeDTO;
import com.gca.dto.trainee.TraineeUpdateData;
import com.gca.model.Trainee;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TraineeMapperTest {
    TraineeMapper mapper = new TraineeMapperImpl();

    @Test
    void testToEntity_fromCreateRequest() {
        TraineeCreateDTO request = TraineeCreateDTO.builder()
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .address("Kyiv")
                .build();

        Trainee entity = mapper.toEntity(request);

        assertNotNull(entity);
        assertEquals(LocalDate.of(2000, 1, 1), entity.getDateOfBirth());
        assertEquals("Kyiv", entity.getAddress());
    }

    @Test
    void testToEntity_fromUpdateRequest() {
        TraineeUpdateData request = GymTestProvider.createTraineeUpdateRequest();

        Trainee entity = mapper.toEntity(request);

        assertEquals(request.getDateOfBirth(), entity.getDateOfBirth());
        assertEquals(request.getAddress(), entity.getAddress());
    }

    @Test
    void testToResponse() {
        Trainee expected = GymTestProvider.constructTrainee();

        TraineeDTO response = mapper.toResponse(expected);

        assertNotNull(response);
        assertEquals(expected.getUser().getUsername(), response.getUsername());
        assertEquals(expected.getUser().getFirstName(), response.getFirstName());
        assertEquals(expected.getUser().getLastName(), response.getLastName());
    }
}