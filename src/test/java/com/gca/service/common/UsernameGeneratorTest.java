package com.gca.service.common;

import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

import com.gca.dao.TraineeDAO;
import com.gca.dao.TrainerDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UsernameGeneratorTest {

    private UsernameGenerator sut;
    private TraineeDAO traineeDAO;
    private TrainerDAO trainerDAO;

    @BeforeEach
    void setUp() {
        sut = new UsernameGenerator();

        traineeDAO = mock(TraineeDAO.class);
        trainerDAO = mock(TrainerDAO.class);

        sut.setTraineeDAO(traineeDAO);
        sut.setTrainerDAO(trainerDAO);
    }

    @Test
    void generate_shouldReturnBaseNameIfNotExists() {
        when(traineeDAO.getAllUsernames()).thenReturn(emptyList());
        when(trainerDAO.getAllUsernames()).thenReturn(emptyList());

        String result = sut.generate("John", "Doe");

        assertEquals("John.Doe", result);
    }

    @Test
    void generate_shouldReturnWithSuffixIfBaseTaken() {
        when(traineeDAO.getAllUsernames()).thenReturn(Arrays.asList("John.Doe"));
        when(trainerDAO.getAllUsernames()).thenReturn(emptyList());

        String result = sut.generate("John", "Doe");

        assertEquals("John.Doe1", result);
    }

    @Test
    void generate_shouldIterateSuffixes() {
        when(traineeDAO.getAllUsernames()).thenReturn(Arrays.asList("John.Doe", "John.Doe1"));
        when(trainerDAO.getAllUsernames()).thenReturn(Collections.singletonList("John.Doe2"));

        String result = sut.generate("John", "Doe");

        assertEquals("John.Doe3", result);
    }

    @Test
    void generate_shouldBeCaseSensitive() {
        when(traineeDAO.getAllUsernames()).thenReturn(Collections.singletonList("john.doe"));
        when(trainerDAO.getAllUsernames()).thenReturn(emptyList());

        String result = sut.generate("John", "Doe");

        assertEquals("John.Doe", result);
    }

    @Test
    void generate_shouldLogIfUsernameExist() {
        when(traineeDAO.getAllUsernames()).thenReturn(List.of("john.doe"));
        when(trainerDAO.getAllUsernames()).thenReturn(emptyList());

        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        ((Logger) LoggerFactory.getLogger(UsernameGenerator.class)).addAppender(listAppender);
        listAppender.start();

        sut.generate("john", "doe");

        assertEquals(2, listAppender.list.size());

        ILoggingEvent secondLog = listAppender.list.get(1);
        assertEquals(Level.WARN, secondLog.getLevel());

        String msg = secondLog.getFormattedMessage();
        assertTrue(msg.equalsIgnoreCase("User with username john.doe already exists, generated alternative username: john.doe1"));
    }
}