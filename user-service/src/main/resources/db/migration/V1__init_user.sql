CREATE TABLE t_users (
    id BIGSERIAL NOT NULL ,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    role VARCHAR(20) CHECK (role IN ('STUDENT', 'STAFF', 'FACULTY')),
    user_type VARCHAR(20) CHECK (user_type IN ('STUDENT', 'STAFF', 'FACULTY')),
    PRIMARY KEY (id)
);