package com.gca.security.aspect;

import com.gca.dao.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

@Aspect
@Component
@AllArgsConstructor
public class MyTransactionAspect {

    private final SessionFactory sessionFactory;

    @Around("@annotation(transactional)")
    public Object wrapInTransaction(ProceedingJoinPoint pjp, Transactional transactional) throws Throwable {
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();

        try {
            Object result = pjp.proceed();

            if (transactional.readOnly()) {
                tx.rollback();
            } else {
                tx.commit();
            }

            return result;
        } catch (Throwable ex) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        }
    }
}
