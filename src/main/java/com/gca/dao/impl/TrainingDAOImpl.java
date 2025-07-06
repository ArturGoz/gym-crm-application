package com.gca.dao.impl;

import com.gca.dao.TrainingDAO;
import com.gca.model.Training;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TrainingDAOImpl implements TrainingDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public TrainingDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Training create(Training training) {
        Session session = sessionFactory.openSession();
        session.persist(training);

        return training;
    }

    @Override
    public Training getById(Long id) {
        Session session = sessionFactory.getCurrentSession();

        return session.find(Training.class, id);
    }
}
