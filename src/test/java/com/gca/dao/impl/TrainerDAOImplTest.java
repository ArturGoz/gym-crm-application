package com.gca.dao.impl;

import com.gca.dao.BaseIntegrationTest;
import com.gca.model.Trainer;
import com.gca.model.TrainingType;
import com.gca.model.User;
import com.github.database.rider.core.api.dataset.DataSet;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

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
