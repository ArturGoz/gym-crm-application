CREATE TABLE refresh_tokens
(
    id          BIGSERIAL PRIMARY KEY,
    token       VARCHAR(512) NOT NULL UNIQUE,
    username    VARCHAR(255) NOT NULL,
    expiry_date TIMESTAMP    NOT NULL
);
