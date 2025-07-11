package com.gca;

import com.gca.config.LiquibaseConfig;
import com.gca.config.PersistenceConfig;
import com.gca.dao.impl.UserDAOImpl;
import com.gca.dto.PasswordChangeRequest;
import com.gca.dto.auth.AuthenticationRequest;
import com.gca.dto.trainee.TraineeCreateRequest;
import com.gca.dto.trainee.TraineeUpdateRequest;
import com.gca.dto.user.UserCreateRequest;
import com.gca.dto.user.UserResponse;
import com.gca.facade.TrainingAppFacade;
import com.gca.model.User;
import com.gca.security.AuthenticationService;
import com.gca.service.impl.UserServiceImpl;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext();

        context.register(PersistenceConfig.class, LiquibaseConfig.class);
        context.refresh();

        SessionFactory sessionFactory = context.getBean(SessionFactory.class);
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        try {
            AuthenticationRequest authenticationRequest = new AuthenticationRequest();
            authenticationRequest.setUsername("admin.admin");
            authenticationRequest.setPassword("321qweQWE!");

            AuthenticationService authenticationService =context.getBean(AuthenticationService.class);
            authenticationService.authenticate(authenticationRequest);


            TraineeUpdateRequest traineeCreateRequest = TraineeUpdateRequest.builder()
                    .id(2L)
                    .dateOfBirth(LocalDate.now())
                    .address("32123")
                    .build();

            TrainingAppFacade facade = context.getBean(TrainingAppFacade.class);

            facade.updateTrainee(traineeCreateRequest);

/*            UserCreateRequest userCreateRequest = UserCreateRequest.builder()
                    .firstName("admin")
                    .lastName("admin")
                    .username("admin")
                    .password("123qweQWE!")
                    .build();

            UserServiceImpl userService = context.getBean(UserServiceImpl.class);

            UserResponse response = userService.createUser(userCreateRequest);

            TrainingAppFacade facade = context.getBean(TrainingAppFacade.class);

            TraineeCreateRequest request = TraineeCreateRequest.builder()
                    .address("admin")
                    .dateOfBirth(LocalDate.of(2000,1,1))
                    .userId(response.getId())
                    .build();

            facade.createTrainee(request);*/

            session.getTransaction().commit();
        } catch (Exception ex) {
            session.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            context.close();
        }
    }
}