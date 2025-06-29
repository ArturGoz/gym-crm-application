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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TrainingMapperTest {
    TrainingMapper mapper = new TrainingMapperImpl();

    @Test
    void testToEntity_fromCreateRequest() {
        TrainingCreateRequest request = new TrainingCreateRequest();
        request.setTrainerId(1L);
        request.setTraineeId(2L);
        request.setTrainingDate(LocalDate.of(2025, 6, 28));
        request.setTrainingDuration(Duration.ofMinutes(90));
        request.setTrainingName("Functional Training");
        request.setTrainingType(new TrainingType("Functional"));

        Training entity = mapper.toEntity(request);

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
        Training training = Training.builder()
                .id(10L)
                .trainerId(1L)
                .traineeId(2L)
                .trainingDate(LocalDate.of(2025, 6, 29))
                .trainingDuration(Duration.ofHours(2))
                .trainingName("Morning Yoga")
                .trainingType(new TrainingType("Yoga"))
                .build();

        TrainingResponse response = mapper.toResponse(training);

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
