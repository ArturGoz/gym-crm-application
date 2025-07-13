package com.gca.security.aspect;

import com.gca.dao.transaction.Transactional;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MyTransactionAspectTest {

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Transaction tx;

    @Mock
    private ProceedingJoinPoint pjp;

    @Mock
    private Transactional transactional;

    @InjectMocks
    private MyTransactionAspect aspect;

    @Test
    @SneakyThrows
    void wrapInTransaction_shouldCommit_whenNotReadOnly() {
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(tx);
        when(transactional.readOnly()).thenReturn(false);
        when(pjp.proceed()).thenReturn("result");

        Object result = aspect.wrapInTransaction(pjp, transactional);

        verify(tx).commit();
        verify(tx, never()).rollback();
    }

    @Test
    @SneakyThrows
    void wrapInTransaction_shouldRollback_whenReadOnly() {
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(tx);
        when(transactional.readOnly()).thenReturn(true);
        when(pjp.proceed()).thenReturn("result");

        Object result = aspect.wrapInTransaction(pjp, transactional);

        verify(tx).rollback();
        verify(tx, never()).commit();
    }

    @Test
    void wrapInTransaction_shouldRollbackOnException() throws Throwable {
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(tx);
        when(pjp.proceed()).thenThrow(new RuntimeException("Some error"));
        when(tx.isActive()).thenReturn(true);

        assertThrows(RuntimeException.class, () -> aspect.wrapInTransaction(pjp, transactional));

        verify(tx).rollback();
        verify(tx, never()).commit();
    }
}