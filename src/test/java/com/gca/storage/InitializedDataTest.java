package com.gca.storage;

import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.Training;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InitializedDataTest {

    @Test
    void testEqualsHashCodeToStringSetters() {
        InitializedData d1 = new InitializedData();
        InitializedData d2 = new InitializedData();

        assertEquals(d1, d1);
        assertEquals(d1, d2);
        assertEquals(d1.hashCode(), d2.hashCode());
        assertTrue(d1.canEqual(d2));
        assertNotEquals("not initialized data", d1);
        assertFalse(d1.canEqual("not initialized data"));

        assertNotNull(d1.toString());

        Set<Training> trainings = new HashSet<>();
        Set<Trainer> trainers = new HashSet<>();
        Set<Trainee> trainees = new HashSet<>();

        d1.setTrainings(trainings);
        d1.setTrainers(trainers);
        d1.setTrainees(trainees);

        assertSame(trainings, d1.getTrainings());
        assertSame(trainers, d1.getTrainers());
        assertSame(trainees, d1.getTrainees());
    }
}
