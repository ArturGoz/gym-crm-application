package com.gca.dao.impl;

import com.gca.dao.TrainingDAO;
import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.Training;
import com.gca.model.TrainingType;
import com.gca.model.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TrainingDAOImpl implements TrainingDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public TrainingDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Training create(Training training) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(training);

        return training;
    }

    @Override
    public Training getById(Long id) {
        Session session = sessionFactory.getCurrentSession();

        return session.find(Training.class, id);
    }

    @Override
    public List<Training> getTraineeTrainings(Trainee trainee, LocalDate fromDate,
                                              LocalDate toDate, String trainerName,
                                              String trainingTypeName) {
        QueryContext<Training> ctx = createQueryContext(Training.class);

        ctx.predicates.add(ctx.cb.equal(ctx.root.get("trainee"), trainee));

        addDateRangePredicate(ctx.predicates, ctx.cb, ctx.root.get("date"), fromDate, toDate);

        if (trainerName != null && !trainerName.isEmpty()) {
            Join<Training, Trainer> trainerJoin = ctx.root.join("trainer");
            Join<Trainer, User> trainerUserJoin = trainerJoin.join("user");
            addUserNamePredicate(ctx.predicates, ctx.cb, trainerUserJoin, trainerName);
        }

        if (trainingTypeName != null && !trainingTypeName.isEmpty()) {
            Join<Training, TrainingType> typeJoin = ctx.root.join("type");
            ctx.predicates.add(ctx.cb.like(ctx.cb.lower(typeJoin.get("name")), "%" + trainingTypeName.toLowerCase() + "%"));
        }

        ctx.cq.select(ctx.root).where(ctx.predicates.toArray(new Predicate[0]));

        return ctx.session.createQuery(ctx.cq).getResultList();
    }

    @Override
    public List<Training> getTrainerTrainings(Trainer trainer, LocalDate fromDate,
                                              LocalDate toDate, String traineeName) {
        QueryContext<Training> ctx = createQueryContext(Training.class);

        ctx.predicates.add(ctx.cb.equal(ctx.root.get("trainer"), trainer));

        addDateRangePredicate(ctx.predicates, ctx.cb, ctx.root.get("date"), fromDate, toDate);

        if (traineeName != null && !traineeName.isEmpty()) {
            Join<Training, Trainee> traineeJoin = ctx.root.join("trainee");
            Join<Trainee, User> traineeUserJoin = traineeJoin.join("user");
            addUserNamePredicate(ctx.predicates, ctx.cb, traineeUserJoin, traineeName);
        }

        ctx.cq.select(ctx.root).where(ctx.predicates.toArray(new Predicate[0]));

        return ctx.session.createQuery(ctx.cq).getResultList();
    }

    public static class QueryContext<T> {
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
    }

    private <T> QueryContext<T> createQueryContext(Class<T> entityClass) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);

        return new QueryContext<>(session, cb, cq, root);
    }


    private void addDateRangePredicate(List<Predicate> predicates,
                                       CriteriaBuilder cb,
                                       Path<LocalDate> datePath,
                                       LocalDate fromDate,
                                       LocalDate toDate) {
        if (fromDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(datePath, fromDate));
        }
        if (toDate != null) {
            predicates.add(cb.lessThanOrEqualTo(datePath, toDate));
        }
    }

    private void addUserNamePredicate(List<Predicate> predicates,
                                      CriteriaBuilder cb,
                                      Join<?, User> userJoin,
                                      String username) {
        if (username != null && !username.isEmpty()) {
            predicates.add(cb.like(cb.lower(userJoin.get("username")), "%" + username.toLowerCase() + "%"));
        }
    }

}
