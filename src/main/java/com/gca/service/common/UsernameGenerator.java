package com.gca.service.common;

import com.gca.dao.TraineeDAO;
import com.gca.dao.TrainerDAO;
import com.gca.dao.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class UsernameGenerator {
    private static final Logger logger = LoggerFactory.getLogger(UsernameGenerator.class);

    private UserDAO userDAO;

    @Autowired
    public void setTraineeDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public String generate(String firstName, String lastName) {
        logger.debug("Generating username");
        String base = (firstName + "." + lastName).toLowerCase();
        Set<String> allUsernames = retrieveAllExistUsernames();

        String candidate = base;
        int suffix = 1;

        while (allUsernames.contains(candidate)) {
            candidate = base + suffix++;
        }

        if (!candidate.equalsIgnoreCase(base)) {
            logger.warn("User with username {} already exists, generated alternative username: {}", base, candidate);
        }

        return candidate;
    }

    private Set<String> retrieveAllExistUsernames() {
        return userDAO.getAllUsernames().stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }
}
