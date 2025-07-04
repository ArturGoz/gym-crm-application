-- Table: users
CREATE TABLE users
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50)  NOT NULL,
    last_name  VARCHAR(50)  NOT NULL,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    is_active  BOOLEAN      NOT NULL
);

-- Table: training_types
CREATE TABLE training_types
(
    id                 BIGINT PRIMARY KEY AUTO_INCREMENT,
    training_type_name VARCHAR(100) NOT NULL UNIQUE
);

-- Table: trainers
CREATE TABLE trainers
(
    id                BIGINT PRIMARY KEY AUTO_INCREMENT,
    specialization_id BIGINT NOT NULL,
    user_id           BIGINT NOT NULL,
    CONSTRAINT fk_trainer_specialization FOREIGN KEY (specialization_id) REFERENCES training_types (id),
    CONSTRAINT fk_trainer_user FOREIGN KEY (user_id) REFERENCES users (id)
);

-- Table: trainees
CREATE TABLE trainees
(
    id            BIGINT PRIMARY KEY AUTO_INCREMENT,
    date_of_birth DATE,
    address       VARCHAR(255),
    user_id       BIGINT NOT NULL,
    CONSTRAINT fk_trainee_user FOREIGN KEY (user_id) REFERENCES users (id)
);

-- Table: trainings
CREATE TABLE trainings
(
    id                BIGINT PRIMARY KEY AUTO_INCREMENT,
    training_name     VARCHAR(100) NOT NULL,
    training_date     DATE         NOT NULL,
    training_duration BIGINT       NOT NULL,
    trainee_id        BIGINT       NOT NULL,
    trainer_id        BIGINT       NOT NULL,
    type_id           BIGINT       NOT NULL,
    CONSTRAINT fk_training_trainee FOREIGN KEY (trainee_id) REFERENCES trainees (id),
    CONSTRAINT fk_training_trainer FOREIGN KEY (trainer_id) REFERENCES trainers (id),
    CONSTRAINT fk_training_type FOREIGN KEY (type_id) REFERENCES training_types (id)
);

-- Table: trainee_trainer (Many-to-Many)
CREATE TABLE trainee_trainer
(
    trainee_id BIGINT NOT NULL,
    trainer_id BIGINT NOT NULL,
    PRIMARY KEY (trainee_id, trainer_id),
    CONSTRAINT fk_tt_trainee FOREIGN KEY (trainee_id) REFERENCES trainees (id),
    CONSTRAINT fk_tt_trainer FOREIGN KEY (trainer_id) REFERENCES trainers (id)
);