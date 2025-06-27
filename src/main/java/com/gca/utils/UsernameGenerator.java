package com.gca.utils;

import java.util.function.Predicate;

public interface UsernameGenerator {
    String generate(String firstName, String lastName, Predicate<String> existsPredicate);
}
