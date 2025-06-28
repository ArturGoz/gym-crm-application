package mapper;

import com.gca.dto.trainer.TrainerCreateRequest;
import com.gca.dto.trainer.TrainerResponse;
import com.gca.dto.trainer.TrainerUpdateRequest;
import com.gca.mapper.TrainerMapper;
import com.gca.mapper.TrainerMapperImpl;
import com.gca.model.Trainer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrainerMapperTest {
    TrainerMapper trainerMapper = new TrainerMapperImpl();

    @Test
    void testToEntity_fromCreateRequest() {
        TrainerCreateRequest request = new TrainerCreateRequest(
                "John", "Doe","Crossfit");

        Trainer entity = trainerMapper.toEntity(request);

        assertEquals("John", entity.getFirstName());
        assertEquals("Doe", entity.getLastName());
        assertEquals("Crossfit", entity.getSpecialization());
    }

    @Test
    void testToEntity_fromUpdateRequest() {
        TrainerUpdateRequest request = new TrainerUpdateRequest(
                20L, false, "Yoga"
        );

        Trainer entity = trainerMapper.toEntity(request);

        assertEquals(20L, entity.getId());
        assertFalse(entity.getIsActive());
        assertEquals("Yoga", entity.getSpecialization());
    }

    @Test
    void testToResponse() {
        Trainer entity = new Trainer();
        entity.setId(30L);
        entity.setFirstName("Alice");
        entity.setLastName("Smith");
        entity.setUsername("alice.smith");
        entity.setIsActive(true);
        entity.setSpecialization("Pilates");

        TrainerResponse response = trainerMapper.toResponse(entity);

        assertEquals(30L, response.getId());
        assertEquals("Alice", response.getFirstName());
        assertEquals("Smith", response.getLastName());
        assertEquals("alice.smith", response.getUsername());
        assertTrue(response.getIsActive());
        assertEquals("Pilates", response.getSpecialization());
    }
}
