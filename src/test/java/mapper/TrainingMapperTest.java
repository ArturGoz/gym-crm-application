package mapper;

import com.gca.dto.training.TrainingCreateRequest;
import com.gca.dto.training.TrainingResponse;
import com.gca.mapper.TrainingMapper;
import com.gca.mapper.TrainingMapperImpl;
import com.gca.model.Training;
import com.gca.model.TrainingType;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TrainingMapperTest {
    TrainingMapper trainingMapper = new TrainingMapperImpl();

    @Test
    void testToEntity_fromCreateRequest() {
        TrainingCreateRequest request = new TrainingCreateRequest();
        request.setTrainerId(1L);
        request.setTraineeId(2L);
        request.setTrainingDate(LocalDate.of(2025, 6, 28));
        request.setTrainingDuration(Duration.ofMinutes(90));
        request.setTrainingName("Functional Training");
        request.setTrainingType(new TrainingType("Functional"));

        Training entity = trainingMapper.toEntity(request);

        assertEquals(1L, entity.getTrainerId());
        assertEquals(2L, entity.getTraineeId());
        assertEquals(LocalDate.of(2025, 6, 28), entity.getTrainingDate());
        assertEquals(Duration.ofMinutes(90), entity.getTrainingDuration());
        assertEquals("Functional Training", entity.getTrainingName());
        assertNotNull(entity.getTrainingType());
        assertEquals("Functional", entity.getTrainingType().getName());
    }

    @Test
    void testToResponse() {
        Training training = new Training();
        training.setId(10L);
        training.setTrainerId(1L);
        training.setTraineeId(2L);
        training.setTrainingDate(LocalDate.of(2025, 6, 29));
        training.setTrainingDuration(Duration.ofHours(2));
        training.setTrainingName("Morning Yoga");
        training.setTrainingType(new TrainingType("Yoga"));

        TrainingResponse response = trainingMapper.toResponse(training);

        assertEquals(10L, response.getId());
        assertEquals(1L, response.getTrainerId());
        assertEquals(2L, response.getTraineeId());
        assertEquals(LocalDate.of(2025, 6, 29), response.getTrainingDate());
        assertEquals(Duration.ofHours(2), response.getTrainingDuration());
        assertEquals("Morning Yoga", response.getTrainingName());
        assertNotNull(response.getTrainingType());
        assertEquals("Yoga", response.getTrainingType().getName());
    }
}
