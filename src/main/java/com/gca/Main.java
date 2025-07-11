package com.gca;

import com.gca.config.AppConfig;
import com.gca.config.PersistenceConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext();

        context.register(AppConfig.class, PersistenceConfig.class);
        context.refresh();

        context.close();
    }
}