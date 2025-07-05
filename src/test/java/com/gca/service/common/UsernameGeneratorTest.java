package com.gca.service.common;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.gca.dao.UserDAO;
import com.gca.dao.impl.UserDAOImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UsernameGeneratorTest {

    private UserDAO userDAO;
    private UsernameGenerator sut;

    @BeforeEach
    void setUp() {
        userDAO = mock(UserDAOImpl.class);
        sut = new UsernameGenerator();
        sut.setUserDAO(userDAO);
    }

    @Test
    void generate_shouldReturnBaseNameIfNotExists() {
        when(userDAO.getAllUsernames()).thenReturn(emptyList());

        String result = sut.generate("John", "Doe");

        assertEquals("john.doe", result);
    }

    @Test
    void generate_shouldReturnWithSuffixIfBaseTaken() {
        when(userDAO.getAllUsernames()).thenReturn(Arrays.asList("John.Doe"));

        String result = sut.generate("John", "Doe");

        assertEquals("john.doe1", result);
    }

    @Test
    void generate_shouldIterateSuffixes() {
        when(userDAO.getAllUsernames()).thenReturn(Arrays.asList("John.Doe", "John.Doe1"));

        String result = sut.generate("John", "Doe");

        assertEquals("john.doe2", result);
    }

    @Test
    void generate_shouldTreatCaseAsTheSame() {
        when(userDAO.getAllUsernames()).thenReturn(Collections.singletonList("john.doe"));

        String result = sut.generate("John", "Doe");

        assertEquals("john.doe1", result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"John.doE", "john.doe", "John.doe", "JOHN.DOE", "JohN.doE"})
    void generate_shouldLogIfUsernameExist(String username) {
        when(userDAO.getAllUsernames()).thenReturn(List.of("John.doE"));

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