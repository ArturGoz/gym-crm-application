import com.gca.dto.trainee.TraineeCreateRequest;
import com.gca.dto.trainee.TraineeResponse;
import com.gca.dto.trainee.TraineeUpdateRequest;
import com.gca.mapper.TraineeMapper;
import com.gca.mapper.TraineeMapperImpl;
import com.gca.model.Trainee;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TraineeMapperTest {
    TraineeMapper traineeMapper = new TraineeMapperImpl();

    @Test
    void testToEntity_fromCreateRequest() {
        TraineeCreateRequest request = new TraineeCreateRequest(
                "John", "Doe", LocalDate.of(2000, 1, 1), "Kyiv"
        );

        Trainee entity = traineeMapper.toEntity(request);

        assertEquals("John", entity.getFirstName());
        assertEquals("Doe", entity.getLastName());
        assertEquals(LocalDate.of(2000, 1, 1), entity.getDateOfBirth());
        assertEquals("Kyiv", entity.getAddress());
    }

    @Test
    void testToEntity_fromUpdateRequest() {
        TraineeUpdateRequest request = new TraineeUpdateRequest(
                10L, false, LocalDate.of(1999, 5, 5), "Lviv"
        );

        Trainee entity = traineeMapper.toEntity(request);

        assertEquals(10L, entity.getUserId());
        assertFalse(entity.isActive());
        assertEquals(LocalDate.of(1999, 5, 5), entity.getDateOfBirth());
        assertEquals("Lviv", entity.getAddress());
    }

    @Test
    void testToResponse() {
        Trainee entity = new Trainee();
        entity.setUserId(15L);
        entity.setFirstName("Alice");
        entity.setLastName("Smith");
        entity.setUsername("alice.smith");
        entity.setActive(true);
        entity.setDateOfBirth(LocalDate.of(1998, 12, 12));
        entity.setAddress("Dnipro");

        TraineeResponse response = traineeMapper.toResponse(entity);

        assertEquals(15L, response.getUserId());
        assertEquals("Alice", response.getFirstName());
        assertEquals("Smith", response.getLastName());
        assertEquals("alice.smith", response.getUsername());
        assertTrue(response.isActive());
        assertEquals(LocalDate.of(1998, 12, 12), response.getDateOfBirth());
        assertEquals("Dnipro", response.getAddress());
    }
}
