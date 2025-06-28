package com.gca.service.helper;

import org.springframework.stereotype.Component;

import java.util.function.Predicate;

@Component
public class UsernameGenerator {
    public String generate(String firstName, String lastName, Predicate<String> existsPredicate) {
        String base = firstName + "." + lastName;
        String candidate = base;
        int suffix = 1;

        while (existsPredicate.test(candidate)) {
            candidate = base + suffix++;
        }

        return candidate;
    }
}
