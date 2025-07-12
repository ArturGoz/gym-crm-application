package com.gca;

import com.gca.config.AppConfig;
import com.gca.config.PersistenceConfig;
import com.gca.dto.user.UserResponse;
import com.gca.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext();

        context.register(AppConfig.class, PersistenceConfig.class);
        context.refresh();

        UserService userService = context.getBean(UserService.class);
        UserResponse userResponse = userService.getUserById(1L);
        System.out.println(userResponse);

        context.close();
    }
}