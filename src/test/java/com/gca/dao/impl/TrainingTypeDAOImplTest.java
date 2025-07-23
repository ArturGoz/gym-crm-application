package com.gca.dao.impl;

import com.gca.dao.BaseIntegrationTest;
import com.gca.model.TrainingType;
import com.github.database.rider.core.api.dataset.DataSet;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    void shouldFindAllTrainingTypes() {
        Session session = sessionFactory.getCurrentSession();

        List<TrainingType> expectedList = List.of(
                session.find(TrainingType.class, 1L),
                session.find(TrainingType.class, 2L),
                session.find(TrainingType.class, 3L)

        );
        List<TrainingType> actual = dao.findAllTrainingTypes();

        assertNotNull(actual, "List must not be null");
        assertEquals(3, actual.size(), "Must return 3 training types");
        assertTrue(actual.containsAll(expectedList));
    }
}