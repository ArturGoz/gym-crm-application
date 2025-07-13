package com.gca.mapper;

import com.gca.dto.trainee.TraineeCreateRequest;
import com.gca.dto.trainee.TraineeDTO;
import com.gca.dto.trainee.TraineeUpdateRequest;
import com.gca.model.Trainee;
import com.gca.model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TraineeMapperTest {
    private static final Long USER_ID = 42L;
    private static final String USERNAME = "testuser";
    private static final String FIRSTNAME = "John";
    private static final String LASTNAME = "Doe";
    private static final boolean IS_ACTIVE = true;

    TraineeMapper mapper = new TraineeMapperImpl();

    @Test
    void testToEntity_fromCreateRequest() {
        TraineeCreateRequest request = TraineeCreateRequest.builder()
                .userId(USER_ID)
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
        TraineeUpdateRequest request = TraineeUpdateRequest.builder()
                .userId(USER_ID)
                .dateOfBirth(LocalDate.of(1999, 5, 5))
                .address("Lviv")
                .build();

        Trainee entity = mapper.toEntity(request);

        assertNotNull(entity);
        assertEquals(LocalDate.of(1999, 5, 5), entity.getDateOfBirth());
        assertEquals("Lviv", entity.getAddress());
    }

    @Test
    void testToResponse() {
        User user = buildUser();
        Trainee entity = Trainee.builder()
                .id(42L)
                .user(user)
                .dateOfBirth(LocalDate.of(1998, 12, 12))
                .address("Dnipro")
                .build();

        TraineeDTO response = mapper.toResponse(entity);

        assertNotNull(response);
        assertEquals(42L, response.getId());
        assertEquals("Dnipro", response.getAddress());
        assertEquals(LocalDate.of(1998, 12, 12), response.getDateOfBirth());
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
}