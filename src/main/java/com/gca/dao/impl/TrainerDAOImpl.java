package com.gca.dao.impl;

import com.gca.dao.TrainerDAO;
import com.gca.exception.DaoException;
import com.gca.model.Trainer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TrainerDAOImpl implements TrainerDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public TrainerDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Trainer create(Trainer trainer) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(trainer);

        return trainer;
    }

    @Override
    public Trainer update(Trainer trainer) {
        Session session = sessionFactory.getCurrentSession();
        Trainer existingTrainer = session.find(Trainer.class, trainer.getId());

        if (existingTrainer == null) {
            throw new DaoException("Trainer with id: " + trainer.getId() + " not found");
        }

        return session.merge(trainer);
    }

    @Override
    public Trainer getById(Long id) {
        Session session = sessionFactory.getCurrentSession();

        return session.find(Trainer.class, id);
    }

    @Override
    public Trainer getTrainerByUserId(Long userId) {
        Session session = sessionFactory.getCurrentSession();
        Trainer trainer = session.createQuery(
                        "FROM Trainer t WHERE t.user.id = :userId", Trainer.class)
                .setParameter("userId", userId)
                .uniqueResult();

        if (trainer == null) {
            throw new DaoException("Trainer with user id: " + userId + " not found");
        }

        return trainer;
    }
}