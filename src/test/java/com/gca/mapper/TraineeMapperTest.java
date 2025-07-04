package com.gca.mapper;

import com.gca.dto.trainee.TraineeCreateRequest;
import com.gca.dto.trainee.TraineeResponse;
import com.gca.dto.trainee.TraineeUpdateRequest;
import com.gca.model.Trainee;
import com.gca.model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TraineeMapperTest {
    TraineeMapper mapper = new TraineeMapperImpl();

    private static final Long STATIC_USER_ID = 10L;
    private static final String STATIC_USERNAME = "testuser";
    private static final String STATIC_FIRSTNAME = "John";
    private static final String STATIC_LASTNAME = "Doe";
    private static final boolean STATIC_IS_ACTIVE = true;

    private User buildUser(Long userId, String username, String firstName, String lastName, boolean isActive) {
        return User.builder()
                .id(userId)
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .isActive(isActive)
                .build();
    }

    @Test
    void testToEntity_fromCreateRequest() {
        TraineeCreateRequest request = TraineeCreateRequest.builder()
                .userId(STATIC_USER_ID)
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .address("Kyiv")
                .build();

        Trainee entity = mapper.toEntity(request);

        assertNotNull(entity);
        assertEquals(LocalDate.of(2000, 1, 1), entity.getDateOfBirth());
        assertEquals("Kyiv", entity.getAddress());
        assertNotNull(entity.getUser());
        assertEquals(STATIC_USER_ID, entity.getUser().getId());
    }

    @Test
    void testToEntity_fromUpdateRequest() {
        TraineeUpdateRequest request = TraineeUpdateRequest.builder()
                .userId(STATIC_USER_ID)
                .dateOfBirth(LocalDate.of(1999, 5, 5))
                .address("Lviv")
                .build();

        Trainee entity = mapper.toEntity(request);

        assertNotNull(entity);
        assertEquals(LocalDate.of(1999, 5, 5), entity.getDateOfBirth());
        assertEquals("Lviv", entity.getAddress());
        assertNotNull(entity.getUser());
        assertEquals(STATIC_USER_ID, entity.getUser().getId());
    }

    @Test
    void testToResponse() {
        User user = buildUser(STATIC_USER_ID, "alice.smith", "Alice", "Smith", true);
        Trainee entity = Trainee.builder()
                .id(42L)
                .user(user)
                .dateOfBirth(LocalDate.of(1998, 12, 12))
                .address("Dnipro")
                .build();

        TraineeResponse response = mapper.toResponse(entity);

        assertNotNull(response);
        assertEquals(42L, response.getId());
        assertEquals(STATIC_USER_ID, response.getUserId());
        assertEquals("Dnipro", response.getAddress());
        assertEquals(LocalDate.of(1998, 12, 12), response.getDateOfBirth());
    }
}