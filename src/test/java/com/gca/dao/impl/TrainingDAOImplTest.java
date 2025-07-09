package com.gca.dao.impl;

import com.gca.dao.BaseIntegrationTest;
import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.Training;
import com.gca.model.TrainingType;
import com.github.database.rider.core.api.dataset.DataSet;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


@DataSet(value = "dataset/training/training-data.xml", cleanBefore = true, cleanAfter = true, transactional = true)
public class TrainingDAOImplTest extends BaseIntegrationTest<TrainingDAOImpl> {

    @Test
    void shouldSuccessfullyFindTraining() {
        Training expected = sessionFactory.getCurrentSession().find(Training.class, 1L);

        Training actual = dao.getById(expected.getId());

        assertNotNull(actual, "Training should not be null");
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDuration(), actual.getDuration());
        assertEquals(expected.getDate(), actual.getDate());
        assertEquals(expected.getTrainer().getId(), actual.getTrainer().getId());
        assertEquals(expected.getTrainee().getId(), actual.getTrainee().getId());
        assertEquals(expected.getType().getId(), actual.getType().getId());
    }

    @Test
    void shouldNotFindTraining() {
        Training found = dao.getById(99L);
        assertNull(found, "Training should be null");
    }

    @Test
    @DataSet(value = "dataset/training/training-creation-data.xml", cleanBefore = true, cleanAfter = true, transactional = true)
    void shouldCreateTraining() {
        Training expected = buildTrainingFromExistingData();

        Training actual = dao.create(expected);

        assertNotNull(actual.getId(), "Created training should have ID");
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDuration(), actual.getDuration());
        assertEquals(expected.getDate(), actual.getDate());
        assertEquals(expected.getTrainer().getId(), actual.getTrainer().getId());
        assertEquals(expected.getTrainee().getId(), actual.getTrainee().getId());
        assertEquals(expected.getType().getId(), actual.getType().getId());
    }

    private Training buildTrainingFromExistingData() {
        Session session = sessionFactory.getCurrentSession();
        Trainee trainee = session.find(Trainee.class, 1L);
        Trainer trainer = session.find(Trainer.class, 1L);
        TrainingType type = session.find(TrainingType.class, 1L);

        return Training.builder()
                .name("New Training Session")
                .date(LocalDate.of(2025, 7, 8))
                .duration(90L)
                .trainee(trainee)
                .trainer(trainer)
                .type(type)
                .build();
    }
}
