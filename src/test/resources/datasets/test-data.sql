-- USERS
INSERT INTO users (id, username, first_name, last_name, password, is_active)
VALUES (1, 'finduser', 'John', 'Doe', 'pass', true);

-- TRAINEES
INSERT INTO trainees (id, date_of_birth, address, user_id)
VALUES (1, '2000-01-01', 'Some address', 1);
