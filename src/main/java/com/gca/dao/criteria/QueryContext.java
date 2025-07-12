package com.gca.dao.criteria;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class QueryContext<T> {
    public final Session session;
    public final CriteriaBuilder cb;
    public final CriteriaQuery<T> cq;
    public final Root<T> root;
    public final List<Predicate> predicates = new ArrayList<>();

    public QueryContext(Session session, CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root) {
        this.session = session;
        this.cb = cb;
        this.cq = cq;
        this.root = root;
    }

    public static  <T> QueryContext<T> createQueryContext(Class<T> entityClass, Session session) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);

        return new QueryContext<>(session, cb, cq, root);
    }
}
