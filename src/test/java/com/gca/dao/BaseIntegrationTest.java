package com.gca.dao;

import com.gca.config.LiquibaseConfigTest;
import com.gca.config.PersistenceConfigTest;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.spring.api.DBRider;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        PersistenceConfigTest.class,
        LiquibaseConfigTest.class
})
@ActiveProfiles("test")
@DBRider
@DBUnit(cacheConnection = true, leakHunter = true, caseSensitiveTableNames = false, schema = "PUBLIC")
public abstract class BaseIntegrationTest<T> {

    @Autowired
    protected SessionFactory sessionFactory;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    protected T dao;

    protected Transaction transaction;

    @BeforeEach
    void startTransaction() {
        transaction = sessionFactory.getCurrentSession().beginTransaction();
    }

    @AfterEach
    void rollbackTransaction() {
        if (transaction != null && transaction.isActive()) {
            transaction.rollback();
        }
    }
}
