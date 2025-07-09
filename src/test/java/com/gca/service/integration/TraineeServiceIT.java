package com.gca.service.integration;

import com.gca.dao.TraineeDAO;
import com.gca.dao.UserDAO;
import com.gca.dto.trainee.TraineeCreateRequest;
import com.gca.dto.trainee.TraineeResponse;
import com.gca.dto.trainee.TraineeUpdateRequest;
import com.gca.exception.DaoException;
import com.gca.mapper.TraineeMapper;
import com.gca.model.Trainee;
import com.gca.model.User;
import com.gca.service.impl.TraineeServiceImpl;
import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TraineeServiceIT extends AbstractServiceIT {

    @Autowired
    private TraineeDAO traineeDAO;

    @Autowired
    private UserDAO userDAO;

    @BeforeEach
    void setUp() {
        transaction = sessionFactory.getCurrentSession().beginTransaction();

        TraineeMapper traineeMapper = Mappers.getMapper(TraineeMapper.class);

        traineeService = new TraineeServiceImpl();
        traineeService.setTraineeDAO(traineeDAO);
        traineeService.setUserDAO(userDAO);
        traineeService.setTraineeMapper(traineeMapper);
    }

    @AfterEach
    void tearDown() {
        if (transaction != null && transaction.isActive()) {
            transaction.rollback();
        }
    }

    @Test
    @DataSet(value = "dataset/trainee/trainee-data.xml", cleanBefore = true, cleanAfter = true, transactional = true)
    void shouldGetTraineeById() {
        TraineeResponse actual = traineeService.getTraineeById(1L);

        assertNotNull(actual);
        assertEquals(1L, actual.getUserId());
    }

    @Test
    @DataSet(value = "dataset/trainee/trainee-data.xml", cleanBefore = true, cleanAfter = true, transactional = true)
    void shouldUpdateTrainee() {
        TraineeUpdateRequest updateRequest = TraineeUpdateRequest.builder()
                .id(1L)
                .address("Updated Address")
                .build();

        TraineeResponse actual = traineeService.updateTrainee(updateRequest);

        assertEquals("Updated Address", actual.getAddress());
    }

    @Test
    @DataSet(value = "dataset/trainee/trainee-data.xml", cleanBefore = true, cleanAfter = true, transactional = true)
    void shouldCreateTrainee() {
        User user = userDAO.getById(2L);

        TraineeCreateRequest request = TraineeCreateRequest.builder()
                .userId(user.getId())
                .address("New Address")
                .dateOfBirth(LocalDate.of(2002, 3, 3))
                .build();

        TraineeResponse actual = traineeService.createTrainee(request);

        assertNotNull(actual.getId());
        assertEquals("New Address", actual.getAddress());
    }

    @Test
    @DataSet(value = "dataset/trainee/trainee-data.xml", cleanBefore = true, cleanAfter = true, transactional = true)
    void shouldDeleteTraineeByIdByUsername() {
        String username = "john.doe";

        Trainee before = traineeDAO.findByUsername(username);
        assertNotNull(before);

        traineeService.deleteTraineeByUsername(username);

        DaoException ex = assertThrows(
                DaoException.class,
                () -> traineeDAO.findByUsername(username),
                "Expected DaoException when trainee is not found"
        );

        assertTrue(ex.getMessage().contains("not found"), "Exception message should indicate not found");
    }
}

