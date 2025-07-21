package com.gca.dao.impl;

import com.gca.dao.BaseIntegrationTest;
import com.gca.exception.DaoException;
import com.gca.model.Trainer;
import com.gca.model.TrainingType;
import com.gca.model.User;
import com.github.database.rider.core.api.dataset.DataSet;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataSet(value = "dataset/trainer/trainer-data.xml", cleanBefore = true, cleanAfter = true, transactional = true)
public class TrainerDAOImplTest extends BaseIntegrationTest<TrainerDAOImpl> {

    @Test
    void shouldSuccessfullyFindTrainer() {
        Trainer expected = sessionFactory.getCurrentSession().find(Trainer.class, 1L);

        Trainer actual = dao.getById(expected.getId());

        assertNotNull(actual, "Loaded trainer should not be null");
        assertEquals(expected.getSpecialization().getId(), actual.getSpecialization().getId());
        assertEquals(expected.getUser().getUsername(), actual.getUser().getUsername());
        assertEquals(expected.getUser().getFirstName(), actual.getUser().getFirstName());
        assertEquals(expected.getUser().getLastName(), actual.getUser().getLastName());
    }

    @Test
    void shouldNotFindTrainer() {
        Trainer found = dao.getById(99L);
        assertNull(found, "Trainer should be null");
    }

    @Test
    @DataSet(value = "dataset/trainer/trainer-creation-data.xml", cleanBefore = true, cleanAfter = true, transactional = true)
    void shouldCreateTrainer() {
        Trainer expected = buildTrainerFromExistingUserAndSpecialization();

        Trainer actual = dao.create(expected);

        assertNotNull(actual.getId(), "Created trainer should have ID");

        assertEquals(expected.getUser().getUsername(), actual.getUser().getUsername());
        assertEquals(expected.getSpecialization().getId(), actual.getSpecialization().getId());
        assertEquals(expected.getUser().getFirstName(), actual.getUser().getFirstName());
        assertEquals(expected.getUser().getLastName(), actual.getUser().getLastName());
    }

    @Test
    void shouldUpdateTrainer() {
        Trainer expected = sessionFactory.getCurrentSession().find(Trainer.class, 1L);
        expected.getUser().setFirstName("UpdatedName");

        Trainer actual = dao.update(expected);

        assertEquals(expected.getUser().getFirstName(), actual.getUser().getFirstName());
        assertEquals(expected.getSpecialization().getId(), actual.getSpecialization().getId());
        assertEquals(expected.getUser().getLastName(), actual.getUser().getLastName());
    }

    @Test
    void shouldFindTrainerByUsername() {
        Trainer found = dao.findByUsername("arnold.schwarzenegger");

        assertNotNull(found, "Trainer should be found by username");
        assertEquals("arnold.schwarzenegger", found.getUser().getUsername());
        assertEquals("Arnold", found.getUser().getFirstName());
    }

    @Test
    void shouldThrowIfUsernameNotFound() {
        DaoException exception = assertThrows(DaoException.class, () -> dao.findByUsername("non.existent"));
        assertTrue(exception.getMessage().contains("Trainer with username: non.existent not found"));
    }

    @Test
    void shouldReturnAllTrainers() {
        List<Trainer> actual = dao.getAllTrainers();

        assertNotNull(actual, "List should not be null");
        assertEquals(1, actual.size(), "Should contain one trainer from dataset");
        assertEquals("arnold.schwarzenegger", actual.get(0).getUser().getUsername());
    }

    private Trainer buildTrainerFromExistingUserAndSpecialization() {
        Session session = sessionFactory.getCurrentSession();
        User existingUser = session.find(User.class, 2L);
        TrainingType specialization = session.find(TrainingType.class, 1L);
        return Trainer.builder()
                .user(existingUser)
                .specialization(specialization)
                .build();
    }
}
