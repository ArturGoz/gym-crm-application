package com.gca.dao.impl;

import com.gca.dao.TraineeDAO;
import com.gca.exception.DaoException;
import com.gca.model.Trainee;
import com.gca.model.Trainer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TraineeDAOImpl implements TraineeDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public TraineeDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Trainee create(Trainee trainee) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(trainee);

        return trainee;
    }

    @Override
    public Trainee update(Trainee trainee) {
        Session session = sessionFactory.getCurrentSession();
        Trainee existingTrainee = session.find(Trainee.class, trainee.getId());

        if (existingTrainee == null) {
            throw new DaoException("Trainee with id: " + trainee.getId() + " not found");
        }

        return session.merge(trainee);
    }

    @Override
    public Trainee getById(Long id) {
        Session session = sessionFactory.getCurrentSession();

        return session.find(Trainee.class, id);
    }

    @Override
    public Trainee getTraineeByUserId(Long userId) {
        Session session = sessionFactory.getCurrentSession();
        Trainee trainee = session.createQuery(
                        "FROM Trainee t WHERE t.user.id = :userId", Trainee.class)
                .setParameter("userId", userId)
                .uniqueResult();

        if (trainee == null) {
            throw new DaoException("Trainee with user id: " + userId + " not found");
        }

        return trainee;
    }

    @Override
    public void delete(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Trainee trainee = session.find(Trainee.class, id);

        if (trainee == null) {
            throw new DaoException("Trainee with id: " + id + " not found");
        }
        session.remove(trainee);
    }
}
