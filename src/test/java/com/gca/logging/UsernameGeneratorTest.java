package com.gca.logging;

import com.gca.dao.TraineeDAO;
import com.gca.dao.TrainerDAO;
import com.gca.service.common.UsernameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UsernameGeneratorTest {
    @Mock
    private TraineeDAO traineeDAO;
    @Mock
    private TrainerDAO trainerDAO;

    @Test
    public void testLoggerWarnWhenUsernameConflict() {
        try (MockedStatic<LoggerFactory> mockedLoggerFactory = mockStatic(LoggerFactory.class)) {
            Logger logger = mock(Logger.class);
            mockedLoggerFactory.when(() -> LoggerFactory.getLogger(UsernameGenerator.class))
                    .thenReturn(logger);

            UsernameGenerator usernameGenerator = new UsernameGenerator();
            usernameGenerator.setTraineeDAO(traineeDAO);
            usernameGenerator.setTrainerDAO(trainerDAO);

            when(traineeDAO.getAllUsernames()).thenReturn(List.of("John.Doe"));
            when(trainerDAO.getAllUsernames()).thenReturn(List.of());

            String result = usernameGenerator.generate("John", "Doe");

            assertEquals("John.Doe1", result);
            verify(logger).warn("User with username {} already exists, generated alternative username: {}",
                    "John.Doe", "John.Doe1");
        }
    }
}