package com.gca.dao.impl;

import com.gca.dao.TraineeDAO;
import com.gca.exception.DaoException;
import com.gca.model.Trainee;
import com.gca.model.Trainer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;

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
            throw new DaoException(String.format("Trainee with id: %d not found", trainee.getId()));
        }

        return session.merge(trainee);
    }

    @Override
    public Trainee getById(Long id) {
        Session session = sessionFactory.getCurrentSession();

        return session.find(Trainee.class, id);
    }

    @Override
    public Trainee findByUsername(String username) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                        "SELECT t FROM Trainee t JOIN FETCH t.user u WHERE u.username = :username",
                        Trainee.class
                )
                .setParameter("username", username)
                .uniqueResultOptional()
                .orElseThrow(() -> new DaoException(String.format("Trainee with username: %s not found", username)));
    }

    @Override
    public Trainee updateTraineeTrainers(String traineeUsername, List<String> trainerUsernames) {
        Session session = sessionFactory.getCurrentSession();
        Trainee trainee = findByUsername(traineeUsername);

        List<Trainer> trainers = session.createQuery(
                        "SELECT tr FROM Trainer tr JOIN FETCH tr.user u WHERE u.username IN (:usernames)",
                        Trainer.class
                )
                .setParameter("usernames", trainerUsernames)
                .getResultList();

        if (trainers.size() != trainerUsernames.size()) {
            throw new DaoException("Some trainers were not found for usernames: " + trainerUsernames);
        }

        trainee.setTrainers(new HashSet<>(trainers));

        return session.merge(trainee);
    }

    @Override
    public void deleteById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Trainee trainee = session.find(Trainee.class, id);

        if (trainee == null) {
            throw new DaoException(String.format("Trainee with id: %d not found", id));
        }
        session.remove(trainee);
    }

    @Override
    public void deleteByUsername(String username) {
        Session session = sessionFactory.getCurrentSession();

        int deletedCount = session.createMutationQuery(
                        "DELETE FROM Trainee t WHERE t.user.id = " +
                                "(SELECT u.id FROM User u WHERE u.username = :username)"
                )
                .setParameter("username", username)
                .executeUpdate();

        if (deletedCount == 0) {
            throw new DaoException(String.format("Trainee with username: %s not found for deleting", username));
        }
    }
}
