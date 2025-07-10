package com.gca.service.integration;

import com.gca.config.LiquibaseConfigTest;
import com.gca.config.PersistenceConfigTest;
import com.gca.service.impl.TraineeServiceImpl;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.spring.api.DBRider;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
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
@ComponentScan(basePackages = "com.gca.service")
public class AbstractServiceIT {
    @Autowired
    protected SessionFactory sessionFactory;

    protected TraineeServiceImpl traineeService;

    protected Transaction transaction;
}
