package com.gca.dao.impl;

import com.gca.dao.UserDAO;
import com.gca.exception.DaoException;
import com.gca.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public User create(User entity) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(entity);
        return entity;
    }

    @Override
    public User update(User entity) {
        Session session = sessionFactory.getCurrentSession();
        User existing = session.find(User.class, entity.getId());

        if (existing == null) {
            throw new DaoException(String.format("User with id: %d not found", entity.getId()));
        }

        return session.merge(entity);
    }

    @Override
    public User getById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.find(User.class, id);
    }

    @Override
    public void delete(Long id) {
        Session session = sessionFactory.getCurrentSession();
        User user = session.find(User.class, id);

        if (user == null) {
            throw new DaoException(String.format("User with id: %d not found", id));
        }
        session.remove(user);
    }

    @Override
    public User findByUsername(String username) {
        Session session = sessionFactory.getCurrentSession();

        return session.createQuery(
                        "FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .uniqueResult();
    }

    @Override
    public List<String> getAllUsernames() {
        Session session = sessionFactory.getCurrentSession();

        return session.createQuery(
                        "SELECT u.username FROM User u", String.class)
                .getResultList();
    }
}
