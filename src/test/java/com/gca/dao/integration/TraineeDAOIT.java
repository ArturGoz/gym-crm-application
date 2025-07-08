package com.gca.dao.integration;

import com.gca.dao.impl.TraineeDAOImpl;
import com.gca.model.Trainee;
import com.gca.model.User;
import com.github.database.rider.core.api.dataset.DataSet;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ContextConfiguration(classes = TraineeDAOImpl.class)
@DataSet(value = "dataset/trainee/trainee-data.xml", cleanBefore = true, cleanAfter = true, transactional = true)
class TraineeDAOIT  extends AbstractDaoIntegrationTest<TraineeDAOImpl> {

    private static final LocalDate BIRTHDAY = LocalDate.of(2000, 1, 1);
    private static final String ADDRESS = "Some address";

    @Autowired
    void setDao(TraineeDAOImpl dao) {
        this.dao = dao;
    }

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
    void shouldDeleteTraineeById() {
        dao.delete(1L);
        Trainee trainee = sessionFactory.getCurrentSession().find(Trainee.class, 1L);
        assertNull(trainee);
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

