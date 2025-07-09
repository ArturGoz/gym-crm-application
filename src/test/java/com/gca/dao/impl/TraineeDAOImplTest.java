package com.gca.dao.impl;

import com.gca.dao.BaseIntegrationTest;
import com.gca.model.Trainee;
import com.gca.model.User;
import com.github.database.rider.core.api.dataset.DataSet;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataSet(value = "dataset/trainee/trainee-data.xml", cleanBefore = true, cleanAfter = true, transactional = true)
class TraineeDAOImplTest extends BaseIntegrationTest<TraineeDAOImpl> {

    private static final LocalDate BIRTHDAY = LocalDate.of(2000, 1, 1);
    private static final String ADDRESS = "Some address";

    @Test
    void shouldSuccessfullyFindTrainee() {
        Trainee expected = sessionFactory.getCurrentSession().find(Trainee.class, 1L);

        Trainee actual = dao.getById(expected.getId());

        assertNotNull(actual, "Loaded expected should not be null");
        assertEquals(expected.getUser().getUsername(), actual.getUser().getUsername());
        assertEquals(expected.getUser().getFirstName(), actual.getUser().getFirstName());
        assertEquals(expected.getUser().getLastName(), actual.getUser().getLastName());
        assertEquals(expected.getDateOfBirth(), actual.getDateOfBirth());
    }

    @Test
    void shouldNotFindTrainee() {
        Trainee found = dao.getById(99L);
        assertNull(found, "Found trainee should  be null");
    }

    @Test
    @DataSet(value = "dataset/trainee/trainee-creation-data.xml", cleanBefore = true, cleanAfter = true, transactional = true)
    void shouldCreateTrainee() {
        Trainee expected = buildTraineeFromExistingUser();

        Trainee actual = dao.create(expected);

        assertNotNull(actual.getId(), "Created expected should have ID");

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getUser(), actual.getUser());
        assertEquals(expected.getUser().getUsername(), actual.getUser().getUsername());
        assertEquals(expected.getUser().getFirstName(), actual.getUser().getFirstName());
    }

    @Test
    void shouldUpdateTrainee() {
        Trainee expected = sessionFactory.getCurrentSession().find(Trainee.class, 1L);
        expected.setAddress("Updated Address");

        Trainee actual = dao.update(expected);

        assertEquals(expected.getAddress(), actual.getAddress());
        assertEquals(expected.getUser(), actual.getUser());
        assertEquals(expected.getUser().getUsername(), actual.getUser().getUsername());
        assertEquals(expected.getUser().getFirstName(), actual.getUser().getFirstName());
    }

    @Test
    void shouldDeleteByIdTraineeById() {
        Trainee existing = sessionFactory.getCurrentSession().find(Trainee.class, 1L);
        assertNotNull(existing, "Trainee should exist before delete");

        dao.deleteById(1L);

        Trainee deleted = sessionFactory.getCurrentSession().find(Trainee.class, 1L);

        assertNull(deleted, "Trainee should be deleted from DB");
    }

    private Trainee buildTraineeFromExistingUser() {
        Session session = sessionFactory.getCurrentSession();
        User existingUser = session.find(User.class, 2L);
        return Trainee.builder()
                .dateOfBirth(BIRTHDAY)
                .address(ADDRESS)
                .user(existingUser)
                .build();
    }
}

