package com.gca.service.common;

import com.gca.dao.TraineeDAO;
import com.gca.dao.TrainerDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        when(traineeDAO.getAllUsernames()).thenReturn(Collections.emptyList());
        when(trainerDAO.getAllUsernames()).thenReturn(Collections.emptyList());

        String result = sut.generate("John", "Doe");

        assertEquals("John.Doe", result);
    }

    @Test
    void generate_shouldReturnWithSuffixIfBaseTaken() {
        when(traineeDAO.getAllUsernames()).thenReturn(Arrays.asList("John.Doe"));
        when(trainerDAO.getAllUsernames()).thenReturn(Collections.emptyList());

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
        when(trainerDAO.getAllUsernames()).thenReturn(Collections.emptyList());

        String result = sut.generate("John", "Doe");

        assertEquals("John.Doe", result);
    }
}