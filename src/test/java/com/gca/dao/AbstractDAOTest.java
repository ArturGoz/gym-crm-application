package com.gca.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public abstract class AbstractDAOTest {
    @Mock
    protected SessionFactory sessionFactory;

    @Mock
    protected Session session;
}
