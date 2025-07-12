package com.gca.dao.impl;

import com.gca.dao.BaseIntegrationTest;
import com.gca.model.TrainingType;
import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataSet(value = "dataset/trainingtype/training-type-data.xml", cleanBefore = true, cleanAfter = true, transactional = true)
class TrainingTypeDAOImplTest extends BaseIntegrationTest<TrainingTypeDAOImpl> {

    @Test
    void shouldFindTrainingTypeById() {
        TrainingType trainingType = dao.getById(1L);

        assertNotNull(trainingType, "TrainingType must not be null");
        assertEquals(1L, trainingType.getId(), "ID must match");
        assertEquals("Yoga", trainingType.getName(), "Name must match");
    }


    @Test
    void shouldReturnNullIfNotFound() {
        TrainingType trainingType = dao.getById(999L);

        assertNull(trainingType, "TrainingType must be null if not found");
    }
}