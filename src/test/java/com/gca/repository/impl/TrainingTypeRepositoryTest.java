package com.gca.repository.impl;

import com.gca.model.TrainingType;
import com.gca.repository.BaseIntegrationTest;
import com.gca.repository.TrainingTypeRepository;
import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataSet(value = "dataset/trainingtype/training-type-data.xml", cleanBefore = true, cleanAfter = true, transactional = true)
class TrainingTypeRepositoryTest extends BaseIntegrationTest<TrainingTypeRepository> {

    @Test
    void shouldFindAllTrainingTypes() {
        TrainingType yoga = TrainingType.builder()
                .id(1L)
                .name("Yoga")
                .build();

        TrainingType crossfit = TrainingType.builder()
                .id(2L)
                .name("CrossFit")
                .build();

        TrainingType pilates = TrainingType.builder()
                .id(3L)
                .name("Pilates")
                .build();

        List<TrainingType> expectedList = List.of(yoga, crossfit, pilates);

        List<TrainingType> actual = repository.findAll();

        assertNotNull(actual, "List must not be null");
        assertEquals(3, actual.size(), "Must return 3 training types");
        assertTrue(actual.containsAll(expectedList));
    }
}