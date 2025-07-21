package com.gca.service.integration;

import com.gca.dto.user.UserCredentialsDTO;
import com.gca.utils.GymTestProvider;
import com.gca.dao.TraineeDAO;
import com.gca.dto.trainee.TraineeCreateDTO;
import com.gca.dto.trainee.TraineeUpdateRequestDTO;
import com.gca.dto.trainee.TraineeUpdateResponseDTO;
import com.gca.exception.DaoException;
import com.gca.mapper.TraineeMapper;
import com.gca.mapper.UserMapper;
import com.gca.model.Trainee;
import com.gca.service.UserService;
import com.gca.service.impl.TraineeServiceImpl;
import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TraineeServiceIT extends AbstractServiceIT {

    @Autowired
    private TraineeDAO traineeDAO;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        transaction = sessionFactory.getCurrentSession().beginTransaction();

        TraineeMapper traineeMapper = Mappers.getMapper(TraineeMapper.class);

        traineeService = new TraineeServiceImpl();
        traineeService.setTraineeDAO(traineeDAO);
        traineeService.setTraineeMapper(traineeMapper);
        traineeService.setValidator(validator);
        traineeService.setUserService(userService);
        traineeService.setUserMapper(userMapper);
    }

    @AfterEach
    void tearDown() {
        if (transaction != null && transaction.isActive()) {
            transaction.rollback();
        }
    }

    @Test
    @DataSet(value = "dataset/trainee/trainee-data.xml", cleanBefore = true, cleanAfter = true, transactional = true)
    void shouldUpdateTrainee() {
        TraineeUpdateRequestDTO expected = GymTestProvider.createTraineeUpdateRequestDTO();

        TraineeUpdateResponseDTO actual = traineeService.updateTrainee(expected);

        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getAddress(), actual.getAddress());
        assertEquals(expected.getIsActive(), actual.getIsActive());
        assertEquals(expected.getDateOfBirth(), actual.getDateOfBirth());
    }

    @Test
    @DataSet(value = "dataset/trainee/trainee-creation-data.xml", cleanBefore = true, cleanAfter = true, transactional = true)
    void shouldCreateTrainee() {
        TraineeCreateDTO request = GymTestProvider.createTraineeCreateDTO();

        UserCredentialsDTO actual = traineeService.createTrainee(request);

        String expected = format("%s.%s1", request.getFirstName(), request.getLastName()).toLowerCase();

        assertEquals(expected, actual.getUsername());
    }

    @Test
    @DataSet(value = "dataset/trainee/trainee-data.xml", cleanBefore = true, cleanAfter = true, transactional = true)
    void shouldDeleteTraineeByIdByUsername() {
        String username = "arnold.schwarzenegger";

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

