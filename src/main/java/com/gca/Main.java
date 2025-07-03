package com.gca;

import com.gca.config.LiquibaseConfig;
import com.gca.config.PersistenceConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext();

        context.register(PersistenceConfig.class, LiquibaseConfig.class);
        context.refresh();
        context.close();
    }
}