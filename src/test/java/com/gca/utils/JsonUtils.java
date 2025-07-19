package com.gca.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

    private JsonUtils() {
    }

    public static String asJsonString(Object obj) {
        try {
            return new ObjectMapper()
                    .findAndRegisterModules()
                    .writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert object to JSON", e);
        }
    }
}
