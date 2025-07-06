package com.gca;

import com.gca.config.LiquibaseConfig;
import com.gca.config.PersistenceConfig;
import com.gca.dao.UserDAO;
import com.gca.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext();

        context.register(PersistenceConfig.class, LiquibaseConfig.class);
        context.refresh();

        SessionFactory sessionFactory = context.getBean(SessionFactory.class);
        UserDAO userDAO = context.getBean(UserDAO.class);

        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        User user = User.builder()
                .username("testuser")
                .firstName("John")
                .lastName("Doe")
                .password("secret")
                .isActive(true)
                .build();

        // Виклик CRUD
        userDAO.delete(3L);
        System.out.println("Created Trainee: " + user.getId());

/*        // Read
        User found = userDAO.getById(user.getId());
        System.out.println("Found Trainee: " + found);

        // Update
        user.setIsActive(false);
        userDAO.update(found);

        // Delete
        userDAO.delete(found.getId());*/
        session.getTransaction().commit();
        context.close();
    }
}