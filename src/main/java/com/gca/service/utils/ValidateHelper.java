package com.gca.service.utils;

import com.gca.exception.ServiceException;

public final class ValidateHelper {

    private ValidateHelper() {
    }

    public static <T> void requireNotNull(T obj, String message) {
        if (obj == null) {
            throw new ServiceException(message);
        }
    }

    public static void requireAllNotNull(Object... namedObjects) {
        if (namedObjects.length % 2 != 0) {
            throw new IllegalArgumentException("Arguments must be name1, obj1, name2, obj2, ...");
        }

        StringBuilder missing = new StringBuilder();
        for (int i = 0; i < namedObjects.length; i += 2) {
            String name = (String) namedObjects[i];
            Object obj = namedObjects[i + 1];
            if (obj == null) {
                if (!missing.isEmpty()) {
                    missing.append(", ");
                }
                missing.append(name);
            }
        }

        if (!missing.isEmpty()) {
            throw new ServiceException("Missing required: " + missing);
        }
    }
}