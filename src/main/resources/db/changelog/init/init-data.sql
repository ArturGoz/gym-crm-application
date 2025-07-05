-- USERS
INSERT INTO users (id, first_name, last_name, username, password, is_active)
VALUES (1, 'John', 'Doe', 'johndoe', 'hashed_password_trainer', TRUE);

INSERT INTO users (id, first_name, last_name, username, password, is_active)
VALUES (2, 'Jane', 'Smith', 'janesmith', 'hashed_password_trainee', TRUE);

-- TRAINING TYPES
INSERT INTO training_types (id, training_type_name)
VALUES (1, 'Yoga');

-- TRAINERS
INSERT INTO trainers (id, specialization_id, user_id)
VALUES (1, 1, 1);

-- TRAINEES
INSERT INTO trainees (id, date_of_birth, address, user_id)
VALUES (1, '2000-01-01', '123 Main St', 2);

-- TRAININGS
INSERT INTO trainings (id, training_name, training_date, training_duration, trainee_id, trainer_id, type_id)
VALUES (1, 'Morning Yoga', '2025-07-05', 60, 1, 1, 1);

-- TRAINEE_TRAINER LINK
INSERT INTO trainee_trainer (trainee_id, trainer_id)
VALUES (1, 1);
