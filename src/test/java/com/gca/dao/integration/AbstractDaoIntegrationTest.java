package com.gca.dao.integration;

import com.gca.config.LiquibaseConfig;
import com.gca.config.PersistenceConfig;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.nio.file.Files;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        PersistenceConfig.class,
        LiquibaseConfig.class
})
@ActiveProfiles("test")
public abstract class AbstractDaoIntegrationTest<T> {

    @Autowired
    protected SessionFactory sessionFactory;

    protected Session session;
    protected Transaction transaction;

    protected T dao;

    @BeforeEach
    void startTransaction() {
        session = sessionFactory.getCurrentSession();
        transaction = session.beginTransaction();
    }

    @AfterEach
    void rollbackTransaction() {
        if (transaction != null && transaction.isActive()) {
            transaction.rollback();
        }
    }

    protected String readSqlScript(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        return new String(Files.readAllBytes(resource.getFile().toPath()));
    }
}
