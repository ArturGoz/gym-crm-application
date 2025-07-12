package com.gca.dao.criteria;

import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.Training;
import jakarta.persistence.criteria.Join;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TrainingCriteria {

    public void addTraineeCriteria(QueryContext<Training> ctx, Trainee trainee,
                                          LocalDate fromDate, LocalDate toDate,
                                          String trainerName, String trainingTypeName) {
        ctx.predicates.add(ctx.cb.equal(ctx.root.get("trainee"), trainee));
        addDateRangePredicate(ctx, fromDate, toDate);

        if (trainerName != null && !trainerName.isEmpty()) {
            Join<?, ?> trainerJoin = ctx.root.join("trainer").join("user");
            addUserNamePredicate(ctx, trainerJoin, trainerName);
        }

        if (trainingTypeName != null && !trainingTypeName.isEmpty()) {
            Join<?, ?> typeJoin = ctx.root.join("type");
            ctx.predicates.add(ctx.cb.like(ctx.cb.lower(typeJoin.get("name")),
                    "%" + trainingTypeName.toLowerCase() + "%"));
        }
    }

    public void addTrainerCriteria(QueryContext<Training> ctx, Trainer trainer,
                                          LocalDate fromDate, LocalDate toDate,
                                          String traineeName) {
        ctx.predicates.add(ctx.cb.equal(ctx.root.get("trainer"), trainer));
        addDateRangePredicate(ctx, fromDate, toDate);

        if (traineeName != null && !traineeName.isEmpty()) {
            Join<?, ?> traineeJoin = ctx.root.join("trainee").join("user");
            addUserNamePredicate(ctx, traineeJoin, traineeName);
        }
    }

    private void addDateRangePredicate(QueryContext<Training> ctx,
                                              LocalDate fromDate, LocalDate toDate) {
        if (fromDate != null) {
            ctx.predicates.add(ctx.cb.greaterThanOrEqualTo(ctx.root.get("date"), fromDate));
        }
        if (toDate != null) {
            ctx.predicates.add(ctx.cb.lessThanOrEqualTo(ctx.root.get("date"), toDate));
        }
    }

    private void addUserNamePredicate(QueryContext<Training> ctx,
                                             Join<?, ?> userJoin, String username) {
        ctx.predicates.add(ctx.cb.like(ctx.cb.lower(userJoin.get("username")),
                "%" + username.toLowerCase() + "%"));
    }
}

