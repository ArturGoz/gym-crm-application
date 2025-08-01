package com.gca.repository.criteria;

import com.gca.model.Trainee;
import com.gca.model.Trainer;
import com.gca.model.Training;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TrainingSpecifications {
    private TrainingSpecifications(){

    }

    public static Specification<Training> traineeTrainings(Trainee trainee,
                                                           LocalDate fromDate,
                                                           LocalDate toDate,
                                                           String trainerName,
                                                           String trainingTypeName) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("trainee"), trainee));

            if (fromDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("date"), fromDate));
            }
            if (toDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("date"), toDate));
            }

            if (trainerName != null && !trainerName.isEmpty()) {
                Join<Object, Object> trainerJoin = root.join("trainer").join("user");
                predicates.add(cb.like(cb.lower(trainerJoin.get("username")), "%" + trainerName.toLowerCase() + "%"));
            }

            if (trainingTypeName != null && !trainingTypeName.isEmpty()) {
                Join<Object, Object> typeJoin = root.join("type");
                predicates.add(cb.like(cb.lower(typeJoin.get("name")), "%" + trainingTypeName.toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Training> trainerTrainings(Trainer trainer,
                                                           LocalDate fromDate,
                                                           LocalDate toDate,
                                                           String traineeName) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("trainer"), trainer));

            if (fromDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("date"), fromDate));
            }
            if (toDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("date"), toDate));
            }

            if (traineeName != null && !traineeName.isEmpty()) {
                Join<Object, Object> traineeJoin = root.join("trainee").join("user");
                predicates.add(cb.like(cb.lower(traineeJoin.get("username")), "%" + traineeName.toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

