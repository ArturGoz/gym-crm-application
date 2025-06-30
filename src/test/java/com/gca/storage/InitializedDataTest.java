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
        InitializedData data1 = new InitializedData();
        InitializedData data2 = new InitializedData();

        assertEquals(data1, data1);
        assertEquals(data1, data2);
        assertEquals(data1.hashCode(), data2.hashCode());
        assertTrue(data1.canEqual(data2));
        assertNotEquals("not initialized data", data1);
        assertFalse(data1.canEqual("not initialized data"));

        assertNotNull(data1.toString());

        Set<Training> trainings = new HashSet<>();
        Set<Trainer> trainers = new HashSet<>();
        Set<Trainee> trainees = new HashSet<>();

        data1.setTrainings(trainings);
        data1.setTrainers(trainers);
        data1.setTrainees(trainees);

        assertSame(trainings, data1.getTrainings());
        assertSame(trainers, data1.getTrainers());
        assertSame(trainees, data1.getTrainees());
    }
}
