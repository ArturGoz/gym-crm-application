package com.gca.dao.impl;

import com.gca.dao.TrainingDAO;
import com.gca.dao.criteria.QueryContext;
import com.gca.dao.criteria.TrainingCriteria;
import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.Training;
import jakarta.persistence.criteria.Predicate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class TrainingDAOImpl implements TrainingDAO {

    private final SessionFactory sessionFactory;
    private final TrainingCriteria trainingCriteria;

    @Autowired
    public TrainingDAOImpl(SessionFactory sessionFactory, TrainingCriteria trainingCriteria) {
        this.sessionFactory = sessionFactory;
        this.trainingCriteria = trainingCriteria;
    }

    @Override
    public Training create(Training training) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(training);

        return training;
    }

    @Override
    public List<Training> getTraineeTrainings(Trainee trainee, LocalDate fromDate,
                                              LocalDate toDate, String trainerName,
                                              String trainingTypeName) {
        QueryContext<Training> ctx = QueryContext
                .createQueryContext(Training.class, sessionFactory.getCurrentSession());

        trainingCriteria.addTraineeCriteria(ctx, trainee, fromDate,
                toDate, trainerName, trainingTypeName);

        ctx.cq.select(ctx.root).where(ctx.predicates.toArray(new Predicate[0]));
        return ctx.session.createQuery(ctx.cq).getResultList();
    }

    @Override
    public List<Training> getTrainerTrainings(Trainer trainer, LocalDate fromDate,
                                              LocalDate toDate, String traineeName) {
        QueryContext<Training> ctx = QueryContext
                .createQueryContext(Training.class, sessionFactory.getCurrentSession());

        trainingCriteria.addTrainerCriteria(ctx, trainer, fromDate, toDate, traineeName);

        ctx.cq.select(ctx.root).where(ctx.predicates.toArray(new Predicate[0]));
        return ctx.session.createQuery(ctx.cq).getResultList();
    }
}
