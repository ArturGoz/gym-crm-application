package com.gca.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.ResultActions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

import static java.lang.String.format;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private JsonUtils() {
    }

    public static String asJsonString(Object obj) {
        try {
            return objectMapper
                    .findAndRegisterModules()
                    .writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert object to JSON", e);
        }
    }

    public static <T> T readJson(String path, Class<T> clazz) {
        try {
            String content = Files.readString(Paths.get("src/test/resources/json/" + path));
            return objectMapper.readValue(content, clazz);
        } catch (IOException e) {
            throw new RuntimeException(format("Failed to read JSON from '%s'", path), e);
        }
    }

    public static void assertJsonDate(ResultActions resultActions, String jsonPathPrefix, LocalDate expectedDate) throws Exception {
        resultActions
                .andExpect(jsonPath(jsonPathPrefix + "[0]").value(expectedDate.getYear()))
                .andExpect(jsonPath(jsonPathPrefix + "[1]").value(expectedDate.getMonthValue()))
                .andExpect(jsonPath(jsonPathPrefix + "[2]").value(expectedDate.getDayOfMonth()));
    }
}
