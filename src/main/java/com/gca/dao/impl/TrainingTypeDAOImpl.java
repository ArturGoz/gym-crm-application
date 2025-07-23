package com.gca.dao.impl;

import com.gca.dao.TrainingTypeDAO;
import com.gca.model.TrainingType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TrainingTypeDAOImpl implements TrainingTypeDAO {
    private final SessionFactory sessionFactory;

    @Autowired
    public TrainingTypeDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public TrainingType getById(Long id) {
        Session session = sessionFactory.getCurrentSession();

        return session.find(TrainingType.class, id);
    }

    @Override
    public TrainingType getByName(String name) {
        Session session = sessionFactory.getCurrentSession();

        return session.createQuery(
                        "FROM TrainingType u WHERE u.name = :name", TrainingType.class)
                .setParameter("name", name)
                .uniqueResult();
    }

    @Override
    public List<TrainingType> findAllTrainingTypes() {
        Session session = sessionFactory.getCurrentSession();

        return session.createQuery("FROM TrainingType", TrainingType.class)
                .getResultList();
    }
}
